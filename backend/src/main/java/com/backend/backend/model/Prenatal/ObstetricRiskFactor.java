package com.backend.backend.model.Prenatal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ObstetricRiskFactor")
public class ObstetricRiskFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obstetricRiskFactorID")
    private Integer obstetricRiskFactorID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "multiplePregnancy")
    private Boolean multiplePregnancy;

    @Column(name = "ovarianCyst")
    private Boolean ovarianCyst;

    @Column(name = "myomaUteri")
    private Boolean myomaUteri;

    @Column(name = "thyroidDisorder")
    private Boolean thyroidDisorder;

    @Column(name = "historyOfMiscarriage")
    private Boolean historyOfMiscarriage;

    @Column(name = "stillbirth")
    private Boolean stillbirth;

    @Column(name = "preEclampsia")
    private Boolean preEclampsia;

    @Column(name = "prematureContraction")
    private Boolean prematureContraction;

    // Getters and Setters
    public Integer getObstetricRiskFactorID() {
        return obstetricRiskFactorID;
    }

    public void setObstetricRiskFactorID(Integer obstetricRiskFactorID) {
        this.obstetricRiskFactorID = obstetricRiskFactorID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public Boolean getMultiplePregnancy() {
        return multiplePregnancy;
    }

    public void setMultiplePregnancy(Boolean multiplePregnancy) {
        this.multiplePregnancy = multiplePregnancy;
    }

    public Boolean getOvarianCyst() {
        return ovarianCyst;
    }

    public void setOvarianCyst(Boolean ovarianCyst) {
        this.ovarianCyst = ovarianCyst;
    }

    public Boolean getMyomaUteri() {
        return myomaUteri;
    }

    public void setMyomaUteri(Boolean myomaUteri) {
        this.myomaUteri = myomaUteri;
    }

    public Boolean getThyroidDisorder() {
        return thyroidDisorder;
    }

    public void setThyroidDisorder(Boolean thyroidDisorder) {
        this.thyroidDisorder = thyroidDisorder;
    }

    public Boolean getHistoryOfMiscarriage() {
        return historyOfMiscarriage;
    }

    public void setHistoryOfMiscarriage(Boolean historyOfMiscarriage) {
        this.historyOfMiscarriage = historyOfMiscarriage;
    }

    public Boolean getStillbirth() {
        return stillbirth;
    }

    public void setStillbirth(Boolean stillbirth) {
        this.stillbirth = stillbirth;
    }

    public Boolean getPreEclampsia() {
        return preEclampsia;
    }

    public void setPreEclampsia(Boolean preEclampsia) {
        this.preEclampsia = preEclampsia;
    }

    public Boolean getPrematureContraction() {
        return prematureContraction;
    }

    public void setPrematureContraction(Boolean prematureContraction) {
        this.prematureContraction = prematureContraction;
    }
}
