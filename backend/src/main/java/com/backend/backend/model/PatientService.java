package com.backend.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PatientService")
public class PatientService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patientServiceID")
    private Integer patientServiceID;

    @Column(name = "patientID", nullable = false)
    private Integer patientID;

    @Column(name = "serviceName", nullable = false)
    private String serviceName;

    @Column(name = "employeeName")
    private String employeeName;

    @Column(name = "wardName")
    private String wardName;

    @Column(name = "dateAvailed", nullable = false)
    private LocalDate dateAvailed;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // Getters and Setters
    public Integer getPatientServiceID() {
        return patientServiceID;
    }

    public void setPatientServiceID(Integer patientServiceID) {
        this.patientServiceID = patientServiceID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public LocalDate getDateAvailed() {
        return dateAvailed;
    }

    public void setDateAvailed(LocalDate dateAvailed) {
        this.dateAvailed = dateAvailed;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
