package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MethodCurrentlyUsed")
public class MethodCurrentlyUsed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "methodID")
    private Integer methodID;

    @Column(name = "typeID")
    private Integer typeID;

    @Column(name = "COC")
    private Boolean coc;

    @Column(name = "POP")
    private Boolean pop;

    @Column(name = "Injectable")
    private Boolean injectable;

    @Column(name = "Implant")
    private Boolean implant;

    @Column(name = "IUD_Interval")
    private Boolean iudInterval;

    @Column(name = "IUD_Postpartum")
    private Boolean iudPostpartum;

    @Column(name = "Condom")
    private Boolean condom;

    @Column(name = "BOM_CMM")
    private Boolean bomCmm;

    @Column(name = "BBT")
    private Boolean bbt;

    @Column(name = "STM")
    private Boolean stm;

    @Column(name = "SDM")
    private Boolean sdm;

    @Column(name = "LAM")
    private Boolean lam;

    @Column(name = "OtherMethod", length = 100)
    private String otherMethod;

    // Getters and Setters
    public Integer getMethodID() {
        return methodID;
    }

    public void setMethodID(Integer methodID) {
        this.methodID = methodID;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Boolean getCoc() {
        return coc;
    }

    public void setCoc(Boolean coc) {
        this.coc = coc;
    }

    public Boolean getPop() {
        return pop;
    }

    public void setPop(Boolean pop) {
        this.pop = pop;
    }

    public Boolean getInjectable() {
        return injectable;
    }

    public void setInjectable(Boolean injectable) {
        this.injectable = injectable;
    }

    public Boolean getImplant() {
        return implant;
    }

    public void setImplant(Boolean implant) {
        this.implant = implant;
    }

    public Boolean getIudInterval() {
        return iudInterval;
    }

    public void setIudInterval(Boolean iudInterval) {
        this.iudInterval = iudInterval;
    }

    public Boolean getIudPostpartum() {
        return iudPostpartum;
    }

    public void setIudPostpartum(Boolean iudPostpartum) {
        this.iudPostpartum = iudPostpartum;
    }

    public Boolean getCondom() {
        return condom;
    }

    public void setCondom(Boolean condom) {
        this.condom = condom;
    }

    public Boolean getBomCmm() {
        return bomCmm;
    }

    public void setBomCmm(Boolean bomCmm) {
        this.bomCmm = bomCmm;
    }

    public Boolean getBbt() {
        return bbt;
    }

    public void setBbt(Boolean bbt) {
        this.bbt = bbt;
    }

    public Boolean getStm() {
        return stm;
    }

    public void setStm(Boolean stm) {
        this.stm = stm;
    }

    public Boolean getSdm() {
        return sdm;
    }

    public void setSdm(Boolean sdm) {
        this.sdm = sdm;
    }

    public Boolean getLam() {
        return lam;
    }

    public void setLam(Boolean lam) {
        this.lam = lam;
    }

    public String getOtherMethod() {
        return otherMethod;
    }

    public void setOtherMethod(String otherMethod) {
        this.otherMethod = otherMethod;
    }
}
