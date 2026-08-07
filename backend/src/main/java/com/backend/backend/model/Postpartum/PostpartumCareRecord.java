package com.backend.backend.model.Postpartum;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "postpartum_care_records")
public class PostpartumCareRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postpartumRecordID;

    private Long serviceID;
    private String panNumber;
    private String healthFacilityName;
    private String lastName;
    private String firstName;
    private String middleName;
    private String pin;
    private String houseNumberStreet;
    private String subdivisionBarangay;
    private String cityMunicipality;
    private String province;
    private String zipCode;

    private Integer gravida;
    private Integer paraTerm;
    private Integer paraPreterm;
    private Integer paraAbortion;
    private Integer paraLiving;
    
    private LocalDate dateOfDelivery;

    @Column(columnDefinition = "TEXT")
    private String briefHistory;

    private Boolean referred;
    
    @Column(columnDefinition = "TEXT")
    private String referredReason;

    private Long attendingStaffID;
    private String attendingSignatureName;
    private String patientConformeName;
    private LocalDate dateSigned;

    @OneToMany(mappedBy = "postpartumCareRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostpartumVisit> visits = new ArrayList<>();

    public PostpartumCareRecord() {}

    // Helper method to synchronize bidirectional relationship
    public void addVisit(PostpartumVisit visit) {
        visits.add(visit);
        visit.setPostpartumCareRecord(this);
    }

    public void removeVisit(PostpartumVisit visit) {
        visits.remove(visit);
        visit.setPostpartumCareRecord(null);
    }

    // Getters and Setters
    public Long getPostpartumRecordID() { return postpartumRecordID; }
    public void setPostpartumRecordID(Long postpartumRecordID) { this.postpartumRecordID = postpartumRecordID; }

    public Long getServiceID() { return serviceID; }
    public void setServiceID(Long serviceID) { this.serviceID = serviceID; }

    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }

    public String getHealthFacilityName() { return healthFacilityName; }
    public void setHealthFacilityName(String healthFacilityName) { this.healthFacilityName = healthFacilityName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getHouseNumberStreet() { return houseNumberStreet; }
    public void setHouseNumberStreet(String houseNumberStreet) { this.houseNumberStreet = houseNumberStreet; }

    public String getSubdivisionBarangay() { return subdivisionBarangay; }
    public void setSubdivisionBarangay(String subdivisionBarangay) { this.subdivisionBarangay = subdivisionBarangay; }

    public String getCityMunicipality() { return cityMunicipality; }
    public void setCityMunicipality(String cityMunicipality) { this.cityMunicipality = cityMunicipality; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Integer getGravida() { return gravida; }
    public void setGravida(Integer gravida) { this.gravida = gravida; }

    public Integer getParaTerm() { return paraTerm; }
    public void setParaTerm(Integer paraTerm) { this.paraTerm = paraTerm; }

    public Integer getParaPreterm() { return paraPreterm; }
    public void setParaPreterm(Integer paraPreterm) { this.paraPreterm = paraPreterm; }

    public Integer getParaAbortion() { return paraAbortion; }
    public void setParaAbortion(Integer paraAbortion) { this.paraAbortion = paraAbortion; }

    public Integer getParaLiving() { return paraLiving; }
    public void setParaLiving(Integer paraLiving) { this.paraLiving = paraLiving; }

    public LocalDate getDateOfDelivery() { return dateOfDelivery; }
    public void setDateOfDelivery(LocalDate dateOfDelivery) { this.dateOfDelivery = dateOfDelivery; }

    public String getBriefHistory() { return briefHistory; }
    public void setBriefHistory(String briefHistory) { this.briefHistory = briefHistory; }

    public Boolean getReferred() { return referred; }
    public void setReferred(Boolean referred) { this.referred = referred; }

    public String getReferredReason() { return referredReason; }
    public void setReferredReason(String referredReason) { this.referredReason = referredReason; }

    public Long getAttendingStaffID() { return attendingStaffID; }
    public void setAttendingStaffID(Long attendingStaffID) { this.attendingStaffID = attendingStaffID; }

    public String getAttendingSignatureName() { return attendingSignatureName; }
    public void setAttendingSignatureName(String attendingSignatureName) { this.attendingSignatureName = attendingSignatureName; }

    public String getPatientConformeName() { return patientConformeName; }
    public void setPatientConformeName(String patientConformeName) { this.patientConformeName = patientConformeName; }

    public LocalDate getDateSigned() { return dateSigned; }
    public void setDateSigned(LocalDate dateSigned) { this.dateSigned = dateSigned; }

    public List<PostpartumVisit> getVisits() { return visits; }
    public void setVisits(List<PostpartumVisit> visits) { this.visits = visits; }
}
