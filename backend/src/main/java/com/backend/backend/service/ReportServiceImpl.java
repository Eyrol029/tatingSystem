package com.backend.backend.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.dto.PhilHealthReportDTO;
import com.backend.backend.dto.PhilHealthReportDTO.PatientReportItem;
import com.backend.backend.dto.PhilHealthReportDTO.ServiceBreakdownItem;
import com.backend.backend.dto.PhilHealthReportDTO.SummaryStats;
import com.backend.backend.model.Admission;
import com.backend.backend.model.ClinicalService;
import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.repository.AdmissionRepository;
import com.backend.backend.repository.ClinicalServiceRepository;
import com.backend.backend.repository.FamilyPlanning.FamilyPlanningRecordRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;
import com.backend.backend.service.Prenatal.HighRiskAssessmentService;
import com.backend.backend.service.Prenatal.PrenatalRecordService;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    @Autowired
    private ClinicalServiceRepository clinicalServiceRepository;

    @Autowired
    private AdmissionRepository admissionRepository;

    @Autowired
    private FamilyPlanningRecordRepository familyPlanningRecordRepository;

    @Autowired
    private PrenatalRecordService prenatalRecordService;

    @Autowired
    private HighRiskAssessmentService highRiskAssessmentService;

    @Override
    public PhilHealthReportDTO getPhilHealthSummary(LocalDate start, LocalDate end, String filterService, String filterCategory) {
        PhilHealthReportDTO report = new PhilHealthReportDTO();

        // 1. Fetch all reference data
        List<Patient> allPatients = patientRepository.findAll();
        Map<Integer, Patient> patientMap = allPatients.stream()
                .collect(Collectors.toMap(Patient::getPatientID, p -> p, (p1, p2) -> p1));

        List<ClinicalService> allCatalogServices = clinicalServiceRepository.findAll();
        Map<String, ClinicalService> catalogServiceMap = new HashMap<>();
        for (ClinicalService cs : allCatalogServices) {
            if (cs.getName() != null) {
                catalogServiceMap.put(cs.getName().trim().toLowerCase(), cs);
            }
        }

        List<Admission> allAdmissions = admissionRepository.findAll();
        Set<Integer> philHealthPatientIds = new HashSet<>();
        for (Admission adm : allAdmissions) {
            if (Boolean.TRUE.equals(adm.getHasPhilHealth()) && adm.getPatientID() != null) {
                philHealthPatientIds.add(adm.getPatientID());
            }
        }

        List<FamilyPlanningRecord> allFpRecords = familyPlanningRecordRepository.findAll();
        Map<Integer, String> fpPhilHealthMap = new HashMap<>();
        for (FamilyPlanningRecord fp : allFpRecords) {
            if (fp.getClientID() != null && fp.getPhilHealthNumber() != null && !fp.getPhilHealthNumber().trim().isEmpty()) {
                philHealthPatientIds.add(fp.getClientID());
                fpPhilHealthMap.put(fp.getClientID(), fp.getPhilHealthNumber().trim());
            }
        }

        // 2. Fetch all PatientServices and filter by date range
        List<PatientService> allPatientServices = patientServiceRepository.findAll();
        List<PatientService> inRangeServices = allPatientServices.stream()
                .filter(ps -> {
                    LocalDate date = ps.getDateAvailed();
                    if (date == null) return false;
                    if (start != null && date.isBefore(start)) return false;
                    if (end != null && date.isAfter(end)) return false;
                    return true;
                })
                .collect(Collectors.toList());

        // 3. Build PatientReportItems
        List<PatientReportItem> items = new ArrayList<>();

        for (PatientService ps : inRangeServices) {
            Patient p = ps.getPatientID() != null ? patientMap.get(ps.getPatientID()) : null;
            String serviceName = ps.getServiceName() != null ? ps.getServiceName().trim() : "General Consultation";
            
            // Determine category and case number
            ClinicalService catalogItem = catalogServiceMap.get(serviceName.toLowerCase());
            String caseNumber = ps.getCaseNumber();
            if (caseNumber == null || caseNumber.trim().isEmpty() || "---".equals(caseNumber.trim())) {
                if (catalogItem != null && catalogItem.getCaseNumber() != null && !catalogItem.getCaseNumber().trim().isEmpty()) {
                    caseNumber = catalogItem.getCaseNumber().trim();
                } else if (catalogItem != null && catalogItem.getServiceCode() != null && !catalogItem.getServiceCode().trim().isEmpty()) {
                    caseNumber = catalogItem.getServiceCode().trim();
                } else {
                    caseNumber = "—";
                }
            }

            String category = determineCategory(serviceName, catalogItem);

            // Check category & service filter
            if (filterService != null && !filterService.trim().isEmpty() && !"All".equalsIgnoreCase(filterService)) {
                if (!serviceName.equalsIgnoreCase(filterService.trim())) {
                    continue;
                }
            }
            if (filterCategory != null && !filterCategory.trim().isEmpty() && !"All".equalsIgnoreCase(filterCategory)) {
                if (!category.equalsIgnoreCase(filterCategory.trim())) {
                    continue;
                }
            }

            // PhilHealth status
            boolean hasPhilHealth = false;
            String philHealthNo = "";
            if (ps.getPatientID() != null && philHealthPatientIds.contains(ps.getPatientID())) {
                hasPhilHealth = true;
                philHealthNo = fpPhilHealthMap.getOrDefault(ps.getPatientID(), "PhilHealth Member");
            }
            if (ps.getRemarks() != null && (ps.getRemarks().toLowerCase().contains("philhealth") || ps.getRemarks().toLowerCase().contains("phic"))) {
                hasPhilHealth = true;
                if (philHealthNo.isEmpty()) philHealthNo = "PhilHealth Member";
            }
            if (ps.getPaymentStatus() != null && ps.getPaymentStatus().toLowerCase().contains("philhealth")) {
                hasPhilHealth = true;
                if (philHealthNo.isEmpty()) philHealthNo = "PhilHealth Member";
            }

            PatientReportItem item = new PatientReportItem();
            item.setPatientServiceID(ps.getPatientServiceID());
            item.setPatientID(ps.getPatientID());
            item.setCaseNumber(caseNumber);
            
            if (p != null) {
                String fullName = formatFullName(p.getLName(), p.getFName(), p.getMiddleI());
                item.setPatientName(fullName);
                item.setAge(p.getAge());
                item.setBirthDate(p.getBDate());
                item.setContactNumber(p.getContactNumber() != null ? p.getContactNumber() : "—");
                item.setBarangay(p.getBarangay() != null ? p.getBarangay() : "");
                item.setMunicipality(p.getMunicipality() != null ? p.getMunicipality() : "");
                item.setProvince(p.getProvince() != null ? p.getProvince() : "");
                item.setFullAddress(formatAddress(p.getHouseNo(), p.getStreet(), p.getBarangay(), p.getMunicipality(), p.getProvince()));
            } else {
                item.setPatientName("Patient #" + (ps.getPatientID() != null ? ps.getPatientID() : "N/A"));
                item.setContactNumber("—");
                item.setFullAddress("—");
                item.setBarangay("—");
            }

            item.setServiceName(serviceName);
            item.setServiceCategory(category);
            item.setDateAvailed(ps.getDateAvailed());
            item.setEmployeeName(ps.getEmployeeName() != null && !ps.getEmployeeName().trim().isEmpty() ? ps.getEmployeeName() : "Attending Staff");
            item.setWardName(ps.getWardName() != null ? ps.getWardName() : "—");
            item.setPaymentStatus(ps.getPaymentStatus() != null ? ps.getPaymentStatus() : "Completed");
            item.setHasPhilHealth(hasPhilHealth);
            item.setPhilHealthNumber(philHealthNo.isEmpty() ? "None" : philHealthNo);
            item.setRiskStatus(determineRiskStatus(ps, serviceName));
            item.setRemarks(ps.getRemarks() != null ? ps.getRemarks() : "");

            items.add(item);
        }

        // Sort items by date desc, then patient name
        items.sort((a, b) -> {
            if (a.getDateAvailed() == null && b.getDateAvailed() == null) return 0;
            if (a.getDateAvailed() == null) return 1;
            if (b.getDateAvailed() == null) return -1;
            int cmp = b.getDateAvailed().compareTo(a.getDateAvailed());
            if (cmp != 0) return cmp;
            return String.valueOf(a.getPatientName()).compareToIgnoreCase(String.valueOf(b.getPatientName()));
        });

        report.setPatientRecords(items);

        // 4. Calculate Summary Statistics
        SummaryStats stats = new SummaryStats();
        Set<Integer> uniquePatients = new HashSet<>();
        long totalPrenatal = 0;
        long totalFP = 0;
        long totalDeliveries = 0;
        long totalUltrasound = 0;
        long totalPostpartum = 0;
        long totalLab = 0;
        long totalOther = 0;
        long totalPhilHealth = 0;

        for (PatientReportItem item : items) {
            if (item.getPatientID() != null) {
                uniquePatients.add(item.getPatientID());
            }
            if (Boolean.TRUE.equals(item.getHasPhilHealth())) {
                totalPhilHealth++;
            }

            String cat = item.getServiceCategory().toLowerCase();
            String sName = item.getServiceName().toLowerCase();

            if (cat.contains("prenatal") || sName.contains("prenatal")) {
                totalPrenatal++;
            } else if (cat.contains("family") || cat.contains("fp") || sName.contains("family planning") || sName.contains("fp") || sName.contains("contraceptive") || sName.contains("iud") || sName.contains("implant") || sName.contains("depo")) {
                totalFP++;
            } else if (cat.contains("delivery") || cat.contains("maternity") || cat.contains("admission") || sName.contains("delivery") || sName.contains("admission") || sName.contains("nsd") || sName.contains("lying-in")) {
                totalDeliveries++;
            } else if (cat.contains("ultrasound") || sName.contains("ultrasound") || sName.contains("pelvic") || sName.contains("bpp") || sName.contains("transvaginal")) {
                totalUltrasound++;
            } else if (cat.contains("postpartum") || sName.contains("postpartum")) {
                totalPostpartum++;
            } else if (cat.contains("laboratory") || cat.contains("lab") || sName.contains("laboratory") || sName.contains("urinalysis") || sName.contains("cbc") || sName.contains("blood")) {
                totalLab++;
            } else {
                totalOther++;
            }
        }

        stats.setTotalUniquePatients(uniquePatients.size());
        stats.setTotalServicesAvailed(items.size());
        stats.setTotalPrenatal(totalPrenatal);
        stats.setTotalFamilyPlanning(totalFP);
        stats.setTotalDeliveries(totalDeliveries);
        stats.setTotalUltrasound(totalUltrasound);
        stats.setTotalPostpartum(totalPostpartum);
        stats.setTotalLaboratory(totalLab);
        stats.setTotalOtherServices(totalOther);
        stats.setTotalPhilHealthCovered(totalPhilHealth);
        stats.setTotalNonPhilHealth(items.size() - totalPhilHealth);

        report.setSummary(stats);

        // 5. Calculate Service Breakdown Table (with Case Numbers)
        Map<String, List<PatientReportItem>> groupedByService = items.stream()
                .collect(Collectors.groupingBy(PatientReportItem::getServiceName));

        List<ServiceBreakdownItem> breakdown = new ArrayList<>();
        long totalItems = items.size();

        for (Map.Entry<String, List<PatientReportItem>> entry : groupedByService.entrySet()) {
            String sName = entry.getKey();
            List<PatientReportItem> sItems = entry.getValue();
            long count = sItems.size();
            long phCount = sItems.stream().filter(i -> Boolean.TRUE.equals(i.getHasPhilHealth())).count();
            double pct = totalItems > 0 ? ((double) count / totalItems) * 100.0 : 0.0;
            
            // Get representative case number and category
            String cNumber = sItems.get(0).getCaseNumber();
            String cat = sItems.get(0).getServiceCategory();

            ServiceBreakdownItem bi = new ServiceBreakdownItem(
                    sName,
                    cNumber,
                    cat,
                    count,
                    phCount,
                    Math.round(pct * 10.0) / 10.0
            );
            breakdown.add(bi);
        }

        // Sort breakdown by count descending
        breakdown.sort((a, b) -> Long.compare(b.getCount(), a.getCount()));
        report.setServiceBreakdown(breakdown);

        return report;
    }

    @Override
    public List<String> getAvailableServices() {
        Set<String> services = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        List<ClinicalService> catalog = clinicalServiceRepository.findAll();
        for (ClinicalService cs : catalog) {
            if (cs.getName() != null && !cs.getName().trim().isEmpty()) {
                services.add(cs.getName().trim());
            }
        }
        List<PatientService> patientServices = patientServiceRepository.findAll();
        for (PatientService ps : patientServices) {
            if (ps.getServiceName() != null && !ps.getServiceName().trim().isEmpty()) {
                services.add(ps.getServiceName().trim());
            }
        }
        return new ArrayList<>(services);
    }

    @Override
    public List<String> getAvailableCategories() {
        Set<String> categories = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        categories.add("Prenatal Care");
        categories.add("Family Planning");
        categories.add("Maternity & Delivery");
        categories.add("Ultrasound");
        categories.add("Postpartum Care");
        categories.add("Laboratory");
        categories.add("General Consultation");

        List<ClinicalService> catalog = clinicalServiceRepository.findAll();
        for (ClinicalService cs : catalog) {
            if (cs.getCategory() != null && !cs.getCategory().trim().isEmpty()) {
                categories.add(cs.getCategory().trim());
            }
        }
        return new ArrayList<>(categories);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────────────────────
    private String determineCategory(String serviceName, ClinicalService catalogItem) {
        if (catalogItem != null && catalogItem.getCategory() != null && !catalogItem.getCategory().trim().isEmpty()) {
            return catalogItem.getCategory().trim();
        }
        String lower = serviceName.toLowerCase();
        if (lower.contains("prenatal")) return "Prenatal Care";
        if (lower.contains("family planning") || lower.contains("fp") || lower.contains("contraceptive") || lower.contains("iud") || lower.contains("implant") || lower.contains("depo")) return "Family Planning";
        if (lower.contains("delivery") || lower.contains("maternity") || lower.contains("admission") || lower.contains("nsd") || lower.contains("lying-in")) return "Maternity & Delivery";
        if (lower.contains("ultrasound") || lower.contains("pelvic") || lower.contains("bpp") || lower.contains("transvaginal")) return "Ultrasound";
        if (lower.contains("postpartum")) return "Postpartum Care";
        if (lower.contains("laboratory") || lower.contains("lab") || lower.contains("urinalysis") || lower.contains("cbc") || lower.contains("blood")) return "Laboratory";
        return "General Consultation";
    }

    private String determineRiskStatus(PatientService patientService, String serviceName) {
        if (!serviceName.toLowerCase().contains("prenatal")) return "Normal";

        try {
            List<PrenatalRecord> records = prenatalRecordService.getByServiceID(patientService.getPatientServiceID());
            PrenatalRecord latest = records.stream()
                    .filter(record -> record.getPrenatalrecordID() != null)
                    .max(Comparator.comparing(PrenatalRecord::getPrenatalrecordID))
                    .orElse(null);
            if (latest != null && highRiskAssessmentService.assess(latest.getPrenatalrecordID()).isHighRisk()) {
                return "High Risk";
            }
        } catch (Exception ignored) {
            // Keep report generation available when a prenatal record is incomplete.
        }
        return "Normal";
    }

    private String formatFullName(String lastName, String firstName, String middleInitial) {
        StringBuilder sb = new StringBuilder();
        if (lastName != null && !lastName.trim().isEmpty()) {
            sb.append(lastName.trim()).append(", ");
        }
        if (firstName != null && !firstName.trim().isEmpty()) {
            sb.append(firstName.trim());
        }
        if (middleInitial != null && !middleInitial.trim().isEmpty()) {
            sb.append(" ").append(middleInitial.trim());
            if (!middleInitial.trim().endsWith(".")) {
                sb.append(".");
            }
        }
        return sb.toString().trim();
    }

    private String formatAddress(String houseNo, String street, String barangay, String municipality, String province) {
        List<String> parts = new ArrayList<>();
        if (houseNo != null && !houseNo.trim().isEmpty()) parts.add(houseNo.trim());
        if (street != null && !street.trim().isEmpty()) parts.add(street.trim());
        if (barangay != null && !barangay.trim().isEmpty()) parts.add("Brgy. " + barangay.trim());
        if (municipality != null && !municipality.trim().isEmpty()) parts.add(municipality.trim());
        if (province != null && !province.trim().isEmpty()) parts.add(province.trim());
        if (parts.isEmpty()) return "—";
        return String.join(", ", parts);
    }
}
