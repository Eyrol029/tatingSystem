package com.backend.backend.model.Prenatal;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ConsultationVisits")
public class ConsultationVisits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultationVisitID")
    private Integer consultationVisitID;

    @Column(name = "consultationRecordID")
    private Integer consultationRecordID;

    @Column(name = "visitNumber")
    private Integer visitNumber;

    @Column(name = "visitDate")
    @Temporal(TemporalType.DATE)
    private Date visitDate;

    @Column(name = "ageOfGestationInWeeks")
    private Integer ageOfGestationInWeeks;

    // getters and setters
    public Integer getConsultationVisitID() {
        return consultationVisitID;
    }

    public void setConsultationVisitID(Integer consultationVisitID) {
        this.consultationVisitID = consultationVisitID;
    }

    public Integer getConsultationRecordID() {
        return consultationRecordID;
    }

    public void setConsultationRecordID(Integer consultationRecordID) {
        this.consultationRecordID = consultationRecordID;
    }

    public Integer getVisitNumber() {
        return visitNumber;
    }

    public void setVisitNumber(Integer visitNumber) {
        this.visitNumber = visitNumber;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getAgeOfGestationInWeeks() {
        return ageOfGestationInWeeks;
    }

    public void setAgeOfGestationInWeeks(Integer ageOfGestationInWeeks) {
        this.ageOfGestationInWeeks = ageOfGestationInWeeks;
    }
}
