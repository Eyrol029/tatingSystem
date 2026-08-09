package com.backend.backend.model.FamilyPlanning;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "acknowledgements")
public class Acknowledgement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acknowledgementId; // <--- Note the lower 'd' in Id

    private Long clientID;
    private Long serviceID;
    private String chosenMethod;
    private String clientSignature;
    private LocalDate clientSignatureDate;
    private String wraConsentName;
    private String parentSignature;
    private LocalDate parentSignatureDate;

    public Acknowledgement() {}

    // Getters and Setters
    public Long getAcknowledgementId() { return acknowledgementId; }
    public void setAcknowledgementId(Long acknowledgementId) { this.acknowledgementId = acknowledgementId; }

    public Long getClientID() { return clientID; }
    public void setClientID(Long clientID) { this.clientID = clientID; }

    public Long getServiceID() { return serviceID; }
    public void setServiceID(Long serviceID) { this.serviceID = serviceID; }

    public String getChosenMethod() { return chosenMethod; }
    public void setChosenMethod(String chosenMethod) { this.chosenMethod = chosenMethod; }

    public String getClientSignature() { return clientSignature; }
    public void setClientSignature(String clientSignature) { this.clientSignature = clientSignature; }

    public LocalDate getClientSignatureDate() { return clientSignatureDate; }
    public void setClientSignatureDate(LocalDate clientSignatureDate) { this.clientSignatureDate = clientSignatureDate; }

    public String getWraConsentName() { return wraConsentName; }
    public void setWraConsentName(String wraConsentName) { this.wraConsentName = wraConsentName; }

    public String getParentSignature() { return parentSignature; }
    public void setParentSignature(String parentSignature) { this.parentSignature = parentSignature; }

    public LocalDate getParentSignatureDate() { return parentSignatureDate; }
    public void setParentSignatureDate(LocalDate parentSignatureDate) { this.parentSignatureDate = parentSignatureDate; }
}