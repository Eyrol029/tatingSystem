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

    @Column(name = "prenatalExamDate")
    private Date prenatalExamDate;

    @Column(name = "expectedDeliveryDate")
    private Date expectedDeliveryDate;

    @Column(name = "edc")
    private Date edc;

    @Column(name = "deliveryDate")
    private Date deliveryDate;

    @Column(name = "placeOfDelivery")
    private String placeOfDelivery;

    @Column(name = "typeOfDelivery")
    private String typeOfDelivery;

    @Column(name = "referralHospitalNeeded")
    private Boolean referralHospitalNeeded;

    @Column(name = "referralHospitalName", columnDefinition = "TEXT")
    private String referralHospitalName;

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

    public Date getPrenatalExamDate() {
        return prenatalExamDate;
    }

    public void setPrenatalExamDate(Date prenatalExamDate) {
        this.prenatalExamDate = prenatalExamDate;
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Date getEdc() {
        return edc;
    }

    public void setEdc(Date edc) {
        this.edc = edc;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(String placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public String getTypeOfDelivery() {
        return typeOfDelivery;
    }

    public void setTypeOfDelivery(String typeOfDelivery) {
        this.typeOfDelivery = typeOfDelivery;
    }

    public Boolean getReferralHospitalNeeded() {
        return referralHospitalNeeded;
    }

    public void setReferralHospitalNeeded(Boolean referralHospitalNeeded) {
        this.referralHospitalNeeded = referralHospitalNeeded;
    }

    public String getReferralHospitalName() {
        return referralHospitalName;
    }

    public void setReferralHospitalName(String referralHospitalName) {
        this.referralHospitalName = referralHospitalName;
    }
}
