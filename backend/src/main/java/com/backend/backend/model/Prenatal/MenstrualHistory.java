package com.backend.backend.model.Prenatal;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MenstrualHistory")
public class MenstrualHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menstrualHistoryID")
    private Integer menstrualHistoryID;

    @Column(name = "clinicalHistoryID")
    private Integer clinicalHistoryID;

    @Column(name = "LMP")
    private Date LMP;

    @Column(name = "Gravida")
    private Integer gravida;

    @Column(name = "Para")
    private Integer para;

    @Column(name = "Term")
    private Integer term;

    @Column(name = "Preterm")
    private Integer preterm;

    @Column(name = "Abortion")
    private Integer abortion;

    @Column(name = "Living")
    private Integer living;

    // Getters and Setters
    public Integer getMenstrualHistoryID() {
        return menstrualHistoryID;
    }

    public void setMenstrualHistoryID(Integer menstrualHistoryID) {
        this.menstrualHistoryID = menstrualHistoryID;
    }

    public Integer getClinicalHistoryID() {
        return clinicalHistoryID;
    }

    public void setClinicalHistoryID(Integer clinicalHistoryID) {
        this.clinicalHistoryID = clinicalHistoryID;
    }

    public Date getLMP() {
        return LMP;
    }

    public void setLMP(Date LMP) {
        this.LMP = LMP;
    }

    public Integer getGravida() {
        return gravida;
    }

    public void setGravida(Integer gravida) {
        this.gravida = gravida;
    }

    public Integer getPara() {
        return para;
    }

    public void setPara(Integer para) {
        this.para = para;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getPreterm() {
        return preterm;
    }

    public void setPreterm(Integer preterm) {
        this.preterm = preterm;
    }

    public Integer getAbortion() {
        return abortion;
    }

    public void setAbortion(Integer abortion) {
        this.abortion = abortion;
    }

    public Integer getLiving() {
        return living;
    }

    public void setLiving(Integer living) {
        this.living = living;
    }
}
