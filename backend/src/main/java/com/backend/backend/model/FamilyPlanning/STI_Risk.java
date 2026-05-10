package com.backend.backend.model.familyplanning;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "STI_Risk")
public class STI_Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stiRiskID")
    private Integer stiRiskID;

    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "DateRecorded")
    private Date dateRecorded;

    // Getters and Setters
    public Integer getStiRiskID() {
        return stiRiskID;
    }

    public void setStiRiskID(Integer stiRiskID) {
        this.stiRiskID = stiRiskID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }
}
