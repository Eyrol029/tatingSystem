package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ConsultationRecord")
public class ConsultationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consultationRecordID")
    private Integer consultationRecordID;

    @Column(name = "prenatalRecordID")
    private Integer prenatalRecordID;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // getters and setters
    public Integer getConsultationRecordID() {
        return consultationRecordID;
    }

    public void setConsultationRecordID(Integer consultationRecordID) {
        this.consultationRecordID = consultationRecordID;
    }

    public Integer getPrenatalRecordID() {
        return prenatalRecordID;
    }

    public void setPrenatalRecordID(Integer prenatalRecordID) {
        this.prenatalRecordID = prenatalRecordID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
