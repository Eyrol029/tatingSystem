package com.backend.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.backend.backend.model.Billing.PatientPaymentSummaryDTO;
import com.backend.backend.model.Billing.StatementOfAccount;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.repository.Billing.PaymentInstallmentRepository;
import com.backend.backend.repository.Billing.StatementOfAccountRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.service.Billing.StatementOfAccountServiceImpl;

class StatementOfAccountServiceImplTest {

    @Test
    void shouldUpdateTotalsWhenPaymentIsRecorded() {
        StatementOfAccountServiceImpl service = new StatementOfAccountServiceImpl();
        StatementOfAccountRepository repository = mock(StatementOfAccountRepository.class);
        PaymentInstallmentRepository installmentRepository = mock(PaymentInstallmentRepository.class);
        when(repository.save(any(StatementOfAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(installmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ReflectionTestUtils.setField(service, "repository", repository);
        ReflectionTestUtils.setField(service, "paymentInstallmentRepository", installmentRepository);

        StatementOfAccount soa = new StatementOfAccount();
        soa.setTotalAmount(5000.0);
        soa.setAmountPaid(0.0);
        soa.setBalanceAmount(5000.0);

        service.applyPayment(soa, 1500.0);

        assertEquals(1500.0, soa.getAmountPaid());
        assertEquals(3500.0, soa.getBalanceAmount());
        assertEquals(StatementOfAccount.PaymentStatus.Partial, soa.getPaymentStatus());
    }

    @Test
    void shouldGroupDashboardSummariesByPatient() {
        StatementOfAccountServiceImpl service = new StatementOfAccountServiceImpl();
        StatementOfAccountRepository repository = mock(StatementOfAccountRepository.class);
        PatientServiceRepository patientServiceRepository = mock(PatientServiceRepository.class);
        PatientRepository patientRepository = mock(PatientRepository.class);
        PaymentInstallmentRepository installmentRepository = mock(PaymentInstallmentRepository.class);

        Patient patient = new Patient();
        patient.setPatientID(10);
        patient.setFName("Jane");
        patient.setLName("Doe");
        patient.setEmail("jane@example.com");
        patient.setContactNumber("09170000000");

        PatientService prenatal = new PatientService();
        prenatal.setPatientServiceID(1);
        prenatal.setPatientID(10);
        prenatal.setServiceName("Prenatal");
        prenatal.setServiceFee(1000.0);
        prenatal.setAmountPaid(0.0);

        PatientService familyPlanning = new PatientService();
        familyPlanning.setPatientServiceID(2);
        familyPlanning.setPatientID(10);
        familyPlanning.setServiceName("Family Planning");
        familyPlanning.setServiceFee(500.0);
        familyPlanning.setAmountPaid(100.0);

        StatementOfAccount prenatalSoa = new StatementOfAccount();
        prenatalSoa.setSoaID(1);
        prenatalSoa.setPatientServiceID(1);
        prenatalSoa.setPatientID(10);
        prenatalSoa.setTotalAmount(1000.0);
        prenatalSoa.setAmountPaid(0.0);
        prenatalSoa.setBalanceAmount(1000.0);
        prenatalSoa.setPaymentStatus(StatementOfAccount.PaymentStatus.Pending);

        StatementOfAccount familySoa = new StatementOfAccount();
        familySoa.setSoaID(2);
        familySoa.setPatientServiceID(2);
        familySoa.setPatientID(10);
        familySoa.setTotalAmount(500.0);
        familySoa.setAmountPaid(100.0);
        familySoa.setBalanceAmount(400.0);
        familySoa.setPaymentStatus(StatementOfAccount.PaymentStatus.Partial);

        when(patientServiceRepository.findAll()).thenReturn(List.of(prenatal, familyPlanning));
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientRepository.findById(10)).thenReturn(Optional.of(patient));
        when(repository.findAll()).thenReturn(List.of(prenatalSoa, familySoa));
        when(repository.save(any(StatementOfAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(installmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ReflectionTestUtils.setField(service, "repository", repository);
        ReflectionTestUtils.setField(service, "patientServiceRepository", patientServiceRepository);
        ReflectionTestUtils.setField(service, "patientRepository", patientRepository);
        ReflectionTestUtils.setField(service, "paymentInstallmentRepository", installmentRepository);

        List<PatientPaymentSummaryDTO> summaries = service.getDashboardSummaries();

        assertEquals(1, summaries.size());
        PatientPaymentSummaryDTO dto = summaries.get(0);
        assertEquals(10, dto.getPatientId());
        assertEquals(1500.0, dto.getTotalAmount());
        assertEquals(100.0, dto.getAmountPaid());
        assertEquals(1400.0, dto.getBalanceAmount());
        assertEquals("Partial", dto.getPaymentStatus());
    }
}
