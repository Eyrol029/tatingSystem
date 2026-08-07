package com.backend.backend.model.Postpartum;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "postpartum_visits")
public class PostpartumVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer visitNumber;
    private LocalDate dateOfVisit;
    private String bp;
    private String temperature;
    private String weight;

    private Boolean tetanusImmunization;
    private Boolean mms;
    private Integer mmsQty;
    private Boolean iron;
    private Integer ironQty;
    private Boolean folicAcid;
    private Integer folicAcidQty;
    private Boolean calcium;
    private Integer calciumQty;

    private Boolean counselingMentalHealth;
    private Boolean counselingFamilyPlanning;
    private Boolean counselingNutrition;
    private Boolean lactationManagement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postpartum_record_id")
    @JsonIgnore
    private PostpartumCareRecord postpartumCareRecord;

    public PostpartumVisit() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getVisitNumber() { return visitNumber; }
    public void setVisitNumber(Integer visitNumber) { this.visitNumber = visitNumber; }

    public LocalDate getDateOfVisit() { return dateOfVisit; }
    public void setDateOfVisit(LocalDate dateOfVisit) { this.dateOfVisit = dateOfVisit; }

    public String getBp() { return bp; }
    public void setBp(String bp) { this.bp = bp; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public Boolean getTetanusImmunization() { return tetanusImmunization; }
    public void setTetanusImmunization(Boolean tetanusImmunization) { this.tetanusImmunization = tetanusImmunization; }

    public Boolean getMms() { return mms; }
    public void setMms(Boolean mms) { this.mms = mms; }

    public Integer getMmsQty() { return mmsQty; }
    public void setMmsQty(Integer mmsQty) { this.mmsQty = mmsQty; }

    public Boolean getIron() { return iron; }
    public void setIron(Boolean iron) { this.iron = iron; }

    public Integer getIronQty() { return ironQty; }
    public void setIronQty(Integer ironQty) { this.ironQty = ironQty; }

    public Boolean getFolicAcid() { return folicAcid; }
    public void setFolicAcid(Boolean folicAcid) { this.folicAcid = folicAcid; }

    public Integer getFolicAcidQty() { return folicAcidQty; }
    public void setFolicAcidQty(Integer folicAcidQty) { this.folicAcidQty = folicAcidQty; }

    public Boolean getCalcium() { return calcium; }
    public void setCalcium(Boolean calcium) { this.calcium = calcium; }

    public Integer getCalciumQty() { return calciumQty; }
    public void setCalciumQty(Integer calciumQty) { this.calciumQty = calciumQty; }

    public Boolean getCounselingMentalHealth() { return counselingMentalHealth; }
    public void setCounselingMentalHealth(Boolean counselingMentalHealth) { this.counselingMentalHealth = counselingMentalHealth; }

    public Boolean getCounselingFamilyPlanning() { return counselingFamilyPlanning; }
    public void setCounselingFamilyPlanning(Boolean counselingFamilyPlanning) { this.counselingFamilyPlanning = counselingFamilyPlanning; }

    public Boolean getCounselingNutrition() { return counselingNutrition; }
    public void setCounselingNutrition(Boolean counselingNutrition) { this.counselingNutrition = counselingNutrition; }

    public Boolean getLactationManagement() { return lactationManagement; }
    public void setLactationManagement(Boolean lactationManagement) { this.lactationManagement = lactationManagement; }

    public PostpartumCareRecord getPostpartumCareRecord() { return postpartumCareRecord; }
    public void setPostpartumCareRecord(PostpartumCareRecord postpartumCareRecord) { this.postpartumCareRecord = postpartumCareRecord; }
}