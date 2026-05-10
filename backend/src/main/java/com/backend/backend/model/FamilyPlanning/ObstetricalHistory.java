package com.backend.backend.model.familyplanning;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ObstetricalHistory")
public class ObstetricalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obstetricalHistoryID")
    private Integer obstetricalHistoryID;

    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "Gravida")
    private Integer gravida;

    @Column(name = "Para")
    private Integer para;

    @Column(name = "fullTerm")
    private Integer fullTerm;

    @Column(name = "premature")
    private Integer premature;

    @Column(name = "abortion")
    private Integer abortion;

    @Column(name = "livingChildren")
    private Integer livingChildren;

    @Column(name = "dateOfLastDelivery")
    private Date dateOfLastDelivery;

    @Column(name = "typeOfLastDelivery", length = 50)
    private String typeOfLastDelivery;

    @Column(name = "lastMenstrualPeriod")
    private Date lastMenstrualPeriod;

    @Column(name = "previousMenstrualPeriod")
    private Date previousMenstrualPeriod;

    @Column(name = "menstrualFlowType", length = 20)
    private String menstrualFlowType;

    // getters and setters
    public Integer getObstetricalHistoryID() {
        return obstetricalHistoryID;
    }

    public void setObstetricalHistoryID(Integer obstetricalHistoryID) {
        this.obstetricalHistoryID = obstetricalHistoryID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
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

    public Integer getFullTerm() {
        return fullTerm;
    }

    public void setFullTerm(Integer fullTerm) {
        this.fullTerm = fullTerm;
    }

    public Integer getPremature() {
        return premature;
    }

    public void setPremature(Integer premature) {
        this.premature = premature;
    }

    public Integer getAbortion() {
        return abortion;
    }

    public void setAbortion(Integer abortion) {
        this.abortion = abortion;
    }

    public Integer getLivingChildren() {
        return livingChildren;
    }

    public void setLivingChildren(Integer livingChildren) {
        this.livingChildren = livingChildren;
    }

    public Date getDateOfLastDelivery() {
        return dateOfLastDelivery;
    }

    public void setDateOfLastDelivery(Date dateOfLastDelivery) {
        this.dateOfLastDelivery = dateOfLastDelivery;
    }

    public String getTypeOfLastDelivery() {
        return typeOfLastDelivery;
    }

    public void setTypeOfLastDelivery(String typeOfLastDelivery) {
        this.typeOfLastDelivery = typeOfLastDelivery;
    }

    public Date getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    public void setLastMenstrualPeriod(Date lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    public Date getPreviousMenstrualPeriod() {
        return previousMenstrualPeriod;
    }

    public void setPreviousMenstrualPeriod(Date previousMenstrualPeriod) {
        this.previousMenstrualPeriod = previousMenstrualPeriod;
    }

    public String getMenstrualFlowType() {
        return menstrualFlowType;
    }

    public void setMenstrualFlowType(String menstrualFlowType) {
        this.menstrualFlowType = menstrualFlowType;
    }
}
