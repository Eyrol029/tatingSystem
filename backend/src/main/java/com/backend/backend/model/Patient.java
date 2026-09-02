package com.backend.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientID")
    private Integer patientID;

    @Column(name = "patientCode", length = 50)
    private String patientCode;

    // Personal Details
    @Column(name = "fName", nullable = false, length = 50)
    private String fName;

    @Column(name = "lName", nullable = false, length = 50)
    private String lName;

    @Column(name = "middleI", length = 20)
    private String middleI;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "bDate")
    private java.time.LocalDate bDate;

    // Address
    @Column(name = "HouseNo", length = 50)
    private String houseNo;

    @Column(name = "Street", length = 100)
    private String street;

    @Column(name = "Barangay", length = 100)
    private String barangay;

    @Column(name = "Municipality", length = 100)
    private String municipality;

    @Column(name = "Province", length = 100)
    private String province;

    // Additional Details
    @Column(name = "contactNumber", length = 20)
    private String contactNumber;

    @Column(name = "religion", length = 50)
    private String religion;

    @Column(name = "educationalAttainment", length = 100)
    private String educationalAttainment;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "numberOfPregnancy")
    private Integer numberOfPregnancy = 0;

    @Column(name = "createdAt", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }

    public String getPatientCode() { return patientCode; }
    public void setPatientCode(String patientCode) { this.patientCode = patientCode; }

    public String getFName() { return fName; }
    public void setFName(String fName) { this.fName = fName; }

    public String getLName() { return lName; }
    public void setLName(String lName) { this.lName = lName; }

    public String getMiddleI() { return middleI; }
    public void setMiddleI(String middleI) { this.middleI = middleI; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public java.time.LocalDate getBDate() { return bDate; }
    public void setBDate(java.time.LocalDate bDate) { this.bDate = bDate; }

    public String getHouseNo() { return houseNo; }
    public void setHouseNo(String houseNo) { this.houseNo = houseNo; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getBarangay() { return barangay; }
    public void setBarangay(String barangay) { this.barangay = barangay; }

    public String getMunicipality() { return municipality; }
    public void setMunicipality(String municipality) { this.municipality = municipality; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getEducationalAttainment() { return educationalAttainment; }
    public void setEducationalAttainment(String educationalAttainment) { this.educationalAttainment = educationalAttainment; }

    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }

    public Integer getNumberOfPregnancy() { return numberOfPregnancy; }
    public void setNumberOfPregnancy(Integer numberOfPregnancy) { this.numberOfPregnancy = numberOfPregnancy; }

    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
}