package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RiskForVAW")
public class RiskForVAW {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vawID")
    private Integer vawID;

    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "hasUnpleasantSituation")
    private Boolean hasUnpleasantSituation;

    @Column(name = "partnerDisapproveVisit")
    private Boolean partnerDisapproveVisit;

    @Column(name = "historyOfDomesticViolence")
    private Boolean historyOfDomesticViolence;

    @Column(name = "referredToAgency", length = 100)
    private String referredToAgency;

    // Getters and Setters
    public Integer getVawID() {
        return vawID;
    }

    public void setVawID(Integer vawID) {
        this.vawID = vawID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Boolean getHasUnpleasantSituation() {
        return hasUnpleasantSituation;
    }

    public void setHasUnpleasantSituation(Boolean hasUnpleasantSituation) {
        this.hasUnpleasantSituation = hasUnpleasantSituation;
    }

    public Boolean getPartnerDisapproveVisit() {
        return partnerDisapproveVisit;
    }

    public void setPartnerDisapproveVisit(Boolean partnerDisapproveVisit) {
        this.partnerDisapproveVisit = partnerDisapproveVisit;
    }

    public Boolean getHistoryOfDomesticViolence() {
        return historyOfDomesticViolence;
    }

    public void setHistoryOfDomesticViolence(Boolean historyOfDomesticViolence) {
        this.historyOfDomesticViolence = historyOfDomesticViolence;
    }

    public String getReferredToAgency() {
        return referredToAgency;
    }

    public void setReferredToAgency(String referredToAgency) {
        this.referredToAgency = referredToAgency;
    }
}
