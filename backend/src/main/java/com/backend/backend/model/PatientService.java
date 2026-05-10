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

    @Column(name = "serviceID", nullable = false)
    private Integer serviceID;

    @Column(name = "employeeID")
    private Integer employeeID;

    @Column(name = "wardID")
    private Integer wardID;

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

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getWardID() {
        return wardID;
    }

    public void setWardID(Integer wardID) {
        this.wardID = wardID;
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
