package com.backend.backend.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for PhilHealth & Clinical Service Utilization Reports.
 */
public class PhilHealthReportDTO {

    private SummaryStats summary;
    private List<ServiceBreakdownItem> serviceBreakdown = new ArrayList<>();
    private List<PatientReportItem> patientRecords = new ArrayList<>();

    // ──────────────────────────────────────────────────────────────────────────
    // Summary Statistics
    // ──────────────────────────────────────────────────────────────────────────
    public static class SummaryStats {
        private long totalUniquePatients;
        private long totalServicesAvailed;
        private long totalPrenatal;
        private long totalFamilyPlanning;
        private long totalDeliveries;
        private long totalUltrasound;
        private long totalPostpartum;
        private long totalLaboratory;
        private long totalOtherServices;
        private long totalPhilHealthCovered;
        private long totalNonPhilHealth;

        public long getTotalUniquePatients() { return totalUniquePatients; }
        public void setTotalUniquePatients(long totalUniquePatients) { this.totalUniquePatients = totalUniquePatients; }

        public long getTotalServicesAvailed() { return totalServicesAvailed; }
        public void setTotalServicesAvailed(long totalServicesAvailed) { this.totalServicesAvailed = totalServicesAvailed; }

        public long getTotalPrenatal() { return totalPrenatal; }
        public void setTotalPrenatal(long totalPrenatal) { this.totalPrenatal = totalPrenatal; }

        public long getTotalFamilyPlanning() { return totalFamilyPlanning; }
        public void setTotalFamilyPlanning(long totalFamilyPlanning) { this.totalFamilyPlanning = totalFamilyPlanning; }

        public long getTotalDeliveries() { return totalDeliveries; }
        public void setTotalDeliveries(long totalDeliveries) { this.totalDeliveries = totalDeliveries; }

        public long getTotalUltrasound() { return totalUltrasound; }
        public void setTotalUltrasound(long totalUltrasound) { this.totalUltrasound = totalUltrasound; }

        public long getTotalPostpartum() { return totalPostpartum; }
        public void setTotalPostpartum(long totalPostpartum) { this.totalPostpartum = totalPostpartum; }

        public long getTotalLaboratory() { return totalLaboratory; }
        public void setTotalLaboratory(long totalLaboratory) { this.totalLaboratory = totalLaboratory; }

        public long getTotalOtherServices() { return totalOtherServices; }
        public void setTotalOtherServices(long totalOtherServices) { this.totalOtherServices = totalOtherServices; }

        public long getTotalPhilHealthCovered() { return totalPhilHealthCovered; }
        public void setTotalPhilHealthCovered(long totalPhilHealthCovered) { this.totalPhilHealthCovered = totalPhilHealthCovered; }

        public long getTotalNonPhilHealth() { return totalNonPhilHealth; }
        public void setTotalNonPhilHealth(long totalNonPhilHealth) { this.totalNonPhilHealth = totalNonPhilHealth; }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Service Breakdown Item (with Case Number & Counts)
    // ──────────────────────────────────────────────────────────────────────────
    public static class ServiceBreakdownItem {
        private String serviceName;
        private String caseNumber;
        private String category;
        private long count;
        private long philHealthCount;
        private double percentage;

        public ServiceBreakdownItem() {}

        public ServiceBreakdownItem(String serviceName, String caseNumber, String category, long count, long philHealthCount, double percentage) {
            this.serviceName = serviceName;
            this.caseNumber = caseNumber;
            this.category = category;
            this.count = count;
            this.philHealthCount = philHealthCount;
            this.percentage = percentage;
        }

        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }

        public String getCaseNumber() { return caseNumber; }
        public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }

        public long getPhilHealthCount() { return philHealthCount; }
        public void setPhilHealthCount(long philHealthCount) { this.philHealthCount = philHealthCount; }

        public double getPercentage() { return percentage; }
        public void setPercentage(double percentage) { this.percentage = percentage; }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Patient Report Item (Masterlist Row with Case Number)
    // ──────────────────────────────────────────────────────────────────────────
    public static class PatientReportItem {
        private Integer patientServiceID;
        private Integer patientID;
        private String caseNumber;
        private String patientName;
        private Integer age;
        private LocalDate birthDate;
        private String contactNumber;
        private String barangay;
        private String municipality;
        private String province;
        private String fullAddress;
        private String serviceName;
        private String serviceCategory;
        private LocalDate dateAvailed;
        private String employeeName;
        private String wardName;
        private String paymentStatus;
        private Boolean hasPhilHealth;
        private String philHealthNumber;
        private String riskStatus;
        private String remarks;

        public Integer getPatientServiceID() { return patientServiceID; }
        public void setPatientServiceID(Integer patientServiceID) { this.patientServiceID = patientServiceID; }

        public Integer getPatientID() { return patientID; }
        public void setPatientID(Integer patientID) { this.patientID = patientID; }

        public String getCaseNumber() { return caseNumber; }
        public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }

        public String getPatientName() { return patientName; }
        public void setPatientName(String patientName) { this.patientName = patientName; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

        public String getBarangay() { return barangay; }
        public void setBarangay(String barangay) { this.barangay = barangay; }

        public String getMunicipality() { return municipality; }
        public void setMunicipality(String municipality) { this.municipality = municipality; }

        public String getProvince() { return province; }
        public void setProvince(String province) { this.province = province; }

        public String getFullAddress() { return fullAddress; }
        public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

        public String getServiceName() { return serviceName; }
        public void setServiceName(String serviceName) { this.serviceName = serviceName; }

        public String getServiceCategory() { return serviceCategory; }
        public void setServiceCategory(String serviceCategory) { this.serviceCategory = serviceCategory; }

        public LocalDate getDateAvailed() { return dateAvailed; }
        public void setDateAvailed(LocalDate dateAvailed) { this.dateAvailed = dateAvailed; }

        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public String getWardName() { return wardName; }
        public void setWardName(String wardName) { this.wardName = wardName; }

        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

        public Boolean getHasPhilHealth() { return hasPhilHealth; }
        public void setHasPhilHealth(Boolean hasPhilHealth) { this.hasPhilHealth = hasPhilHealth; }

        public String getPhilHealthNumber() { return philHealthNumber; }
        public void setPhilHealthNumber(String philHealthNumber) { this.philHealthNumber = philHealthNumber; }

        public String getRiskStatus() { return riskStatus; }
        public void setRiskStatus(String riskStatus) { this.riskStatus = riskStatus; }

        public String getRemarks() { return remarks; }
        public void setRemarks(String remarks) { this.remarks = remarks; }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Getters and Setters
    // ──────────────────────────────────────────────────────────────────────────
    public SummaryStats getSummary() { return summary; }
    public void setSummary(SummaryStats summary) { this.summary = summary; }

    public List<ServiceBreakdownItem> getServiceBreakdown() { return serviceBreakdown; }
    public void setServiceBreakdown(List<ServiceBreakdownItem> serviceBreakdown) { this.serviceBreakdown = serviceBreakdown; }

    public List<PatientReportItem> getPatientRecords() { return patientRecords; }
    public void setPatientRecords(List<PatientReportItem> patientRecords) { this.patientRecords = patientRecords; }
}
