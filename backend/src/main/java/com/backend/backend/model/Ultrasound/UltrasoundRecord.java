package com.backend.backend.model.Ultrasound;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UltrasoundRecord")
public class UltrasoundRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ultrasoundRecordID;

    private Integer serviceID;
    private LocalDate ultrasoundDate;

    private Integer gravida;
    private Integer term;
    private Integer preterm;
    private Integer abortion;
    private Integer living;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // Getters and Setters
    public Integer getUltrasoundRecordID() {
        return ultrasoundRecordID;
    }

    public void setUltrasoundRecordID(Integer ultrasoundRecordID) {
        this.ultrasoundRecordID = ultrasoundRecordID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public LocalDate getUltrasoundDate() {
        return ultrasoundDate;
    }

    public void setUltrasoundDate(LocalDate ultrasoundDate) {
        this.ultrasoundDate = ultrasoundDate;
    }

    public Integer getGravida() {
        return gravida;
    }

    public void setGravida(Integer gravida) {
        this.gravida = gravida;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getPreterm() {
        return preterm;
    }

    public void setPreterm(Integer preterm) {
        this.preterm = preterm;
    }

    public Integer getAbortion() {
        return abortion;
    }

    public void setAbortion(Integer abortion) {
        this.abortion = abortion;
    }

    public Integer getLiving() {
        return living;
    }

    public void setLiving(Integer living) {
        this.living = living;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
