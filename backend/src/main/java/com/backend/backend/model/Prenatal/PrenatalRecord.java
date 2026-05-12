package com.backend.backend.model.Prenatal;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PrenatalRecord")
public class PrenatalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prenatalrecordID")
    private Integer prenatalrecordID;

    @Column(name = "serviceID")
    private Integer serviceID;

    @Column(name = "initialPreConsultationDate")
    private Date initialPreConsultationDate;

    // Getters and Setters
    public Integer getPrenatalrecordID() {
        return prenatalrecordID;
    }

    public void setPrenatalrecordID(Integer prenatalrecordID) {
        this.prenatalrecordID = prenatalrecordID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public Date getInitialPreConsultationDate() {
        return initialPreConsultationDate;
    }

    public void setInitialPreConsultationDate(Date initialPreConsultationDate) {
        this.initialPreConsultationDate = initialPreConsultationDate;
    }
}
