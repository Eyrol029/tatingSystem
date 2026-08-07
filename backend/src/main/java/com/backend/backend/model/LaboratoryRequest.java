package com.backend.backend.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "laboratory_requests")
public class LaboratoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate requestDate;
    private String patientName;
    private String address;
    private Integer age;
    private String sex;
    private LocalDate birthDate;
    private String diagnosis;

    // Stores selected tests as a JSON list or element collection table
    @ElementCollection
    @CollectionTable(name = "lab_request_tests", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "test_name")
    private List<String> selectedTests;

    public LaboratoryRequest() {
    }

    public LaboratoryRequest(Long id, LocalDate requestDate, String patientName, String address, 
                             Integer age, String sex, LocalDate birthDate, String diagnosis, 
                             List<String> selectedTests) {
        this.id = id;
        this.requestDate = requestDate;
        this.patientName = patientName;
        this.address = address;
        this.age = age;
        this.sex = sex;
        this.birthDate = birthDate;
        this.diagnosis = diagnosis;
        this.selectedTests = selectedTests;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<String> getSelectedTests() {
        return selectedTests;
    }

    public void setSelectedTests(List<String> selectedTests) {
        this.selectedTests = selectedTests;
    }
}
