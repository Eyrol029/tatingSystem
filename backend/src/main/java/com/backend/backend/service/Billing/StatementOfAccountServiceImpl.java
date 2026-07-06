package com.backend.backend.service.Billing;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Billing.PatientPaymentSummaryDTO;
import com.backend.backend.model.Billing.PaymentInstallment;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.model.Billing.StatementOfAccountDetailsDTO;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.repository.Billing.PaymentInstallmentRepository;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;

@Service
public class StatementOfAccountServiceImpl implements StatementOfAccountService {

    @Autowired
    private StatementOfAccountRepository repository;

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PaymentInstallmentRepository paymentInstallmentRepository;

    @Override
    public StatementOfAccount addSoa(StatementOfAccount soa) {
        if (soa.getTotalAmount() == null || soa.getTotalAmount() == 0.0) {
            soa.setTotalAmount(soa.getSummaryFees());
        }
        if (soa.getBalanceAmount() == null || soa.getBalanceAmount() == 0.0) {
            soa.setBalanceAmount(soa.getTotalAmount() - soa.getAmountPaid());
        }
        return repository.save(soa);
    }

    @Override
    public StatementOfAccount getSoaById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SOA not found: " + id));
    }

    @Override
    public List<StatementOfAccount> getAllSoa() {
        return repository.findAll();
    }

    @Override
    public StatementOfAccount updateSoa(StatementOfAccount soa) {
        return repository.save(soa);
    }

    @Override
    public void deleteSoa(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public void applyPayment(
            StatementOfAccount soa,
            Double amount,
            String notes,
            String discountName,
            Double discountAmount,
            String serviceBreakdown
    ) {
        double paymentAmount = amount == null ? 0.0 : Math.max(0.0, amount);
        double totalAmount = soa.getTotalAmount() == null ? 0.0 : soa.getTotalAmount();
        double currentPaid = soa.getAmountPaid() == null ? 0.0 : soa.getAmountPaid();
        double updatedPaid = Math.min(totalAmount, currentPaid + paymentAmount);

        soa.setAmountPaid(updatedPaid);
        soa.setBalanceAmount(Math.max(0.0, totalAmount - updatedPaid));
        if (updatedPaid <= 0.0) {
            soa.setPaymentStatus(StatementOfAccount.PaymentStatus.Pending);
        } else if (updatedPaid >= totalAmount) {
            soa.setPaymentStatus(StatementOfAccount.PaymentStatus.Paid);
        } else {
            soa.setPaymentStatus(StatementOfAccount.PaymentStatus.Partial);
        }
        repository.save(soa);

        // CHANGED: >= 0 instead of > 0, so a record (with its discount/service breakdown)
        // can be saved even if the patient isn't paying anything on this visit yet.
        if (paymentAmount >= 0.0) {
            PaymentInstallment installment = new PaymentInstallment();
            installment.setSoaId(soa.getSoaID());
            installment.setPatientServiceId(soa.getPatientServiceID());
            installment.setPatientId(soa.getPatientID());
            installment.setAmountPaid(paymentAmount);
            installment.setPaymentDate(java.time.LocalDateTime.now());
            installment.setPaymentMethod("Cash");
            // CHANGED: use the real notes coming from the frontend instead of a hardcoded string
            installment.setNotes(notes != null && !notes.isBlank() ? notes : "Installment payment recorded");
            installment.setInstallmentNumber(soa.getInstallmentCount() == null ? 1 : soa.getInstallmentCount() + 1);

            // NEW: persist the discount + full service/discount breakdown so the
            // frontend can reconstruct the "Availed Services" list when reopening.
            installment.setDiscountName(discountName);
            installment.setDiscountAmount(discountAmount);
            installment.setServiceBreakdown(serviceBreakdown);

            paymentInstallmentRepository.save(installment);
            soa.setInstallmentCount(installment.getInstallmentNumber());
            repository.save(soa);
        }
    }

    @Override
    public List<PatientPaymentSummaryDTO> getDashboardSummaries() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientService> services = patientServiceRepository.findAll();
        List<StatementOfAccount> allSoas = repository.findAll();

        Map<Integer, List<StatementOfAccount>> soasByPatient = allSoas.stream()
                .filter(soa -> soa.getPatientID() != null)
                .collect(Collectors.groupingBy(StatementOfAccount::getPatientID));

        Map<Integer, List<PatientService>> servicesByPatient = services.stream()
                .filter(Objects::nonNull)
                .filter(service -> service.getPatientID() != null)
                .collect(Collectors.groupingBy(PatientService::getPatientID));

        return patients.stream().map(patient -> {
            List<StatementOfAccount> patientSoas = soasByPatient.getOrDefault(patient.getPatientID(), List.of());
            List<PatientService> patientServices = servicesByPatient.getOrDefault(patient.getPatientID(), List.of());

            if (patientSoas.isEmpty() && !patientServices.isEmpty()) {
                patientSoas = patientServices.stream().map(this::createDefaultSoa).toList();
            }

            double totalAmount = patientSoas.stream().mapToDouble(soa -> soa.getTotalAmount() == null ? 0.0 : soa.getTotalAmount()).sum();
            double amountPaid = patientSoas.stream().mapToDouble(soa -> soa.getAmountPaid() == null ? 0.0 : soa.getAmountPaid()).sum();
            double balanceAmount = patientSoas.stream().mapToDouble(soa -> soa.getBalanceAmount() == null ? 0.0 : soa.getBalanceAmount()).sum();
            String paymentStatus = determineOverallPaymentStatus(patientSoas);

            String serviceNames = patientServices.stream()
                    .map(PatientService::getServiceName)
                    .filter(name -> name != null && !name.isBlank())
                    .distinct()
                    .collect(Collectors.joining(", "));
            if (serviceNames.isBlank()) {
                serviceNames = "No services";
            }

            PatientPaymentSummaryDTO dto = new PatientPaymentSummaryDTO();
            dto.setSoaId(patientSoas.isEmpty() ? null : patientSoas.get(0).getSoaID());
            dto.setPatientServiceId(patientSoas.isEmpty() ? null : patientSoas.get(0).getPatientServiceID());
            dto.setPatientId(patient.getPatientID());
            dto.setPatientName(patient.getFName() + " " + patient.getLName());
            dto.setEmail(patient.getEmail());
            dto.setPhone(patient.getContactNumber());
            dto.setServiceName(serviceNames);
            dto.setTotalAmount(totalAmount);
            dto.setAmountPaid(amountPaid);
            dto.setBalanceAmount(balanceAmount);
            dto.setPaymentStatus(paymentStatus);
            dto.setInvoiceReceiptNumber(patientSoas.isEmpty() ? null : patientSoas.get(0).getInvoiceReceiptNumber());
            dto.setDueDate(patientSoas.isEmpty() ? null : patientSoas.get(0).getDueDate());
            return dto;
        }).toList();
    }

    @Override
    public StatementOfAccountDetailsDTO getPatientSoaDetails(Integer patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found: " + patientId));

        List<PatientService> services = patientServiceRepository.findAll().stream()
                .filter(svc -> Objects.equals(svc.getPatientID(), patientId))
                .toList();

        List<StatementOfAccount> patientSoas = repository.findAll().stream()
                .filter(soa -> Objects.equals(soa.getPatientID(), patientId))
                .toList();

        if (patientSoas.isEmpty() && !services.isEmpty()) {
            patientSoas = services.stream().map(this::createDefaultSoa).toList();
        }

        double totalAmount = patientSoas.stream().mapToDouble(soa -> soa.getTotalAmount() == null ? 0.0 : soa.getTotalAmount()).sum();
        double amountPaid = patientSoas.stream().mapToDouble(soa -> soa.getAmountPaid() == null ? 0.0 : soa.getAmountPaid()).sum();
        double balanceAmount = patientSoas.stream().mapToDouble(soa -> soa.getBalanceAmount() == null ? 0.0 : soa.getBalanceAmount()).sum();
        String paymentStatus = determineOverallPaymentStatus(patientSoas);

        String serviceNames = services.stream()
                .map(PatientService::getServiceName)
                .filter(name -> name != null && !name.isBlank())
                .distinct()
                .collect(Collectors.joining(", "));

        StatementOfAccountDetailsDTO dto = new StatementOfAccountDetailsDTO();
        dto.setSoaId(patientSoas.isEmpty() ? null : patientSoas.get(0).getSoaID());
        dto.setPatientId(patientId);
        dto.setPatientName(patient.getFName() + " " + patient.getLName());
        dto.setPatientServiceId(patientSoas.isEmpty() ? null : patientSoas.get(0).getPatientServiceID());
        dto.setServiceName(serviceNames.isBlank() ? "No services" : serviceNames);
        dto.setTotalAmount(totalAmount);
        dto.setAmountPaid(amountPaid);
        dto.setBalanceAmount(balanceAmount);
        dto.setPaymentStatus(paymentStatus);
        dto.setDescription(patientSoas.isEmpty() ? null : patientSoas.get(0).getDescription());
        dto.setDateTimeAdmission(patientSoas.isEmpty() ? null : patientSoas.get(0).getDateTimeAdmission());
        dto.setDueDate(patientSoas.isEmpty() ? null : patientSoas.get(0).getDueDate());
        return dto;
    }

    private String determineOverallPaymentStatus(List<StatementOfAccount> patientSoas) {
        if (patientSoas.isEmpty()) {
            return StatementOfAccount.PaymentStatus.Pending.name();
        }

        double totalAmount = patientSoas.stream().mapToDouble(soa -> soa.getTotalAmount() == null ? 0.0 : soa.getTotalAmount()).sum();
        double amountPaid = patientSoas.stream().mapToDouble(soa -> soa.getAmountPaid() == null ? 0.0 : soa.getAmountPaid()).sum();

        if (amountPaid <= 0.0) {
            return StatementOfAccount.PaymentStatus.Pending.name();
        }
        if (amountPaid >= totalAmount) {
            return StatementOfAccount.PaymentStatus.Paid.name();
        }
        return StatementOfAccount.PaymentStatus.Partial.name();
    }

    private StatementOfAccount createDefaultSoa(PatientService service) {
        StatementOfAccount soa = new StatementOfAccount();
        soa.setPatientServiceID(service.getPatientServiceID());
        soa.setPatientID(service.getPatientID());
        double serviceFee = service.getServiceFee() == null ? 0.0 : service.getServiceFee();
        soa.setTotalAmount(serviceFee);
        soa.setAmountPaid(service.getAmountPaid() == null ? 0.0 : service.getAmountPaid());
        soa.setBalanceAmount(Math.max(0.0, serviceFee - soa.getAmountPaid()));
        soa.setPaymentStatus(service.getAmountPaid() != null && service.getAmountPaid() >= serviceFee ? StatementOfAccount.PaymentStatus.Paid : (service.getAmountPaid() != null && service.getAmountPaid() > 0 ? StatementOfAccount.PaymentStatus.Partial : StatementOfAccount.PaymentStatus.Pending));
        soa.setDescription("SOA for " + service.getServiceName());
        return repository.save(soa);
    }
}