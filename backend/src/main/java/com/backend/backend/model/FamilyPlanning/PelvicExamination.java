package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PelvicExamination")
public class PelvicExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pelvicExamID")
    private Integer pelvicExamID;

    @Column(name = "pExamID")
    private Integer pExamID;

    @Column(name = "Condition")
    private String condition;

    @Column(name = "cervical_Consistency")
    private String cervicalConsistency;

    @Column(name = "cervical_Tenderness")
    private Boolean cervicalTenderness;

    @Column(name = "adnexal_Mass_Tenderness")
    private Boolean adnexalMassTenderness;

    @Column(name = "uterine_Position")
    private String uterinePosition;

    @Column(name = "uterine_Depth")
    private Double uterineDepth;

    public Integer getPelvicExamID() {
        return pelvicExamID;
    }

    public void setPelvicExamID(Integer pelvicExamID) {
        this.pelvicExamID = pelvicExamID;
    }

    public Integer getPExamID() {
        return pExamID;
    }

    public void setPExamID(Integer pExamID) {
        this.pExamID = pExamID;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCervicalConsistency() {
        return cervicalConsistency;
    }

    public void setCervicalConsistency(String cervicalConsistency) {
        this.cervicalConsistency = cervicalConsistency;
    }

    public Boolean getCervicalTenderness() {
        return cervicalTenderness;
    }

    public void setCervicalTenderness(Boolean cervicalTenderness) {
        this.cervicalTenderness = cervicalTenderness;
    }

    public Boolean getAdnexalMassTenderness() {
        return adnexalMassTenderness;
    }

    public void setAdnexalMassTenderness(Boolean adnexalMassTenderness) {
        this.adnexalMassTenderness = adnexalMassTenderness;
    }

    public String getUterinePosition() {
        return uterinePosition;
    }

    public void setUterinePosition(String uterinePosition) {
        this.uterinePosition = uterinePosition;
    }

    public Double getUterineDepth() {
        return uterineDepth;
    }

    public void setUterineDepth(Double uterineDepth) {
        this.uterineDepth = uterineDepth;
    }
}
