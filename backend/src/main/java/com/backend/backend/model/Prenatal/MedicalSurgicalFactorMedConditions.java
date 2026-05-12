package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MedicalSurgicalFactor_medConditions")
public class MedicalSurgicalFactorMedConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medSurgConditionsID")
    private Integer medSurgConditionsID;

    @Column(name = "medsurgID")
    private Integer medsurgID;

    @Column(name = "hypertension")
    private Boolean hypertension;

    @Column(name = "heartDisease")
    private Boolean heartDisease;

    @Column(name = "thyroidDisorder")
    private Boolean thyroidDisorder;

    @Column(name = "obesity")
    private Boolean obesity;

    @Column(name = "moderateToSevereAsthma")
    private Boolean moderateToSevereAsthma;

    @Column(name = "epilepsy")
    private Boolean epilepsy;

    @Column(name = "renalDisease")
    private Boolean renalDisease;

    @Column(name = "bleedingDisorder")
    private Boolean bleedingDisorder;

    @Column(name = "previousCesarianSection")
    private Boolean previousCesarianSection;

    @Column(name = "historyOfMyomectomy")
    private Boolean historyOfMyomectomy;

    // Getters and Setters
    public Integer getMedSurgConditionsID() {
        return medSurgConditionsID;
    }

    public void setMedSurgConditionsID(Integer medSurgConditionsID) {
        this.medSurgConditionsID = medSurgConditionsID;
    }

    public Integer getMedsurgID() {
        return medsurgID;
    }

    public void setMedsurgID(Integer medsurgID) {
        this.medsurgID = medsurgID;
    }

    public Boolean getHypertension() {
        return hypertension;
    }

    public void setHypertension(Boolean hypertension) {
        this.hypertension = hypertension;
    }

    public Boolean getHeartDisease() {
        return heartDisease;
    }

    public void setHeartDisease(Boolean heartDisease) {
        this.heartDisease = heartDisease;
    }

    public Boolean getThyroidDisorder() {
        return thyroidDisorder;
    }

    public void setThyroidDisorder(Boolean thyroidDisorder) {
        this.thyroidDisorder = thyroidDisorder;
    }

    public Boolean getObesity() {
        return obesity;
    }

    public void setObesity(Boolean obesity) {
        this.obesity = obesity;
    }

    public Boolean getModerateToSevereAsthma() {
        return moderateToSevereAsthma;
    }

    public void setModerateToSevereAsthma(Boolean moderateToSevereAsthma) {
        this.moderateToSevereAsthma = moderateToSevereAsthma;
    }

    public Boolean getEpilepsy() {
        return epilepsy;
    }

    public void setEpilepsy(Boolean epilepsy) {
        this.epilepsy = epilepsy;
    }

    public Boolean getRenalDisease() {
        return renalDisease;
    }

    public void setRenalDisease(Boolean renalDisease) {
        this.renalDisease = renalDisease;
    }

    public Boolean getBleedingDisorder() {
        return bleedingDisorder;
    }

    public void setBleedingDisorder(Boolean bleedingDisorder) {
        this.bleedingDisorder = bleedingDisorder;
    }

    public Boolean getPreviousCesarianSection() {
        return previousCesarianSection;
    }

    public void setPreviousCesarianSection(Boolean previousCesarianSection) {
        this.previousCesarianSection = previousCesarianSection;
    }

    public Boolean getHistoryOfMyomectomy() {
        return historyOfMyomectomy;
    }

    public void setHistoryOfMyomectomy(Boolean historyOfMyomectomy) {
        this.historyOfMyomectomy = historyOfMyomectomy;
    }
}
