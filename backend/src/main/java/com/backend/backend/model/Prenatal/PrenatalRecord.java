package com.backend.backend.model.Prenatal;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "initialPreConsultationDate")
    private LocalDate initialPreConsultationDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "prenatalExamDate")
    private LocalDate prenatalExamDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "expectedDeliveryDate")
    private LocalDate expectedDeliveryDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "edc")
    private LocalDate edc;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "deliveryDate")
    private LocalDate deliveryDate;

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

    public LocalDate getInitialPreConsultationDate() {
        return initialPreConsultationDate;
    }

    public void setInitialPreConsultationDate(LocalDate initialPreConsultationDate) {
        this.initialPreConsultationDate = initialPreConsultationDate;
    }

    public LocalDate getPrenatalExamDate() {
        return prenatalExamDate;
    }

    public void setPrenatalExamDate(LocalDate prenatalExamDate) {
        this.prenatalExamDate = prenatalExamDate;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDate getEdc() {
        return edc;
    }

    public void setEdc(LocalDate edc) {
        this.edc = edc;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
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
