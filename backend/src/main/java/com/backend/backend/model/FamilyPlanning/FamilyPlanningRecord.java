package com.backend.backend.model.FamilyPlanning;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FamilyPlanningrecord")
public class FamilyPlanningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fpRecordID")
    private Integer fpRecordID;

    @Column(name = "serviceID", nullable = false)
    private Integer serviceID;

    @Column(name = "clientID", nullable = false)
    private Integer clientID;

    @Column(name = "philHealthNumber", length = 50)
    private String philHealthNumber;

    @Column(name = "civilStatus", length = 20)
    private String civilStatus;

    @Column(name = "averageMonthlyIncome", precision = 10, scale = 2)
    private BigDecimal averageMonthlyIncome;

    @Column(name = "planToHaveMoreChildren", length = 1)
    private String planToHaveMoreChildren;

    @Column(name = "noOfLivingChildren", length = 10)
    private String noOfLivingChildren;

    @Column(name = "dateRegistered")
    private LocalDate dateRegistered;

    // Getters and Setters
    public Integer getFpRecordID() {
        return fpRecordID;
    }

    public void setFpRecordID(Integer fpRecordID) {
        this.fpRecordID = fpRecordID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getPhilHealthNumber() {
        return philHealthNumber;
    }

    public void setPhilHealthNumber(String philHealthNumber) {
        this.philHealthNumber = philHealthNumber;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public BigDecimal getAverageMonthlyIncome() {
        return averageMonthlyIncome;
    }

    public void setAverageMonthlyIncome(BigDecimal averageMonthlyIncome) {
        this.averageMonthlyIncome = averageMonthlyIncome;
    }

    public String getPlanToHaveMoreChildren() {
        return planToHaveMoreChildren;
    }

    public void setPlanToHaveMoreChildren(String planToHaveMoreChildren) {
        this.planToHaveMoreChildren = planToHaveMoreChildren;
    }

    public String getNoOfLivingChildren() {
        return noOfLivingChildren;
    }

    public void setNoOfLivingChildren(String noOfLivingChildren) {
        this.noOfLivingChildren = noOfLivingChildren;
    }

    public LocalDate getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDate dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}
