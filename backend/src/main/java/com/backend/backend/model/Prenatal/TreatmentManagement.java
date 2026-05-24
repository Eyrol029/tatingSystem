package com.backend.backend.model.Prenatal;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "TreatmentManagement")
public class TreatmentManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatmentID")
    private Integer treatmentID;

    @Column(name = "prenatalRecordID")
    private Integer prenatalRecordID;

    @Column(name = "palpitationDone")
    private Boolean palpitationDone;

    @Column(name = "referToRoutineLabExams")
    private Boolean referToRoutineLabExams;

    @Column(name = "advisedDiet")
    private Boolean advisedDiet;

    @Column(name = "advisedHydration")
    private Boolean advisedHydration;

    @Column(name = "advisedExercise")
    private Boolean advisedExercise;

    @Column(name = "hygieneCareEmphasized")
    private Boolean hygieneCareEmphasized;

    @Column(name = "refrainFromHeavyObjects")
    private Boolean refrainFromHeavyObjects;

    @Column(name = "advisedVitaminIntake")
    private Boolean advisedVitaminIntake;

    @Column(name = "discussedFacilityDeliveryImportance")
    private Boolean discussedFacilityDeliveryImportance;

    @Column(name = "referralDoctor", columnDefinition = "TEXT")
    private String referralDoctor;

    @Column(name = "labResultsWithTelephoneOrder", columnDefinition = "TEXT")
    private String labResultsWithTelephoneOrder;

    @Column(name = "tcbOn")
    @Temporal(TemporalType.DATE)
    private Date tcbOn;

    // Getters and Setters
    public Integer getTreatmentID() { return treatmentID; }
    public void setTreatmentID(Integer treatmentID) { this.treatmentID = treatmentID; }

    public Integer getPrenatalRecordID() { return prenatalRecordID; }
    public void setPrenatalRecordID(Integer prenatalRecordID) { this.prenatalRecordID = prenatalRecordID; }

    public Boolean getPalpitationDone() { return palpitationDone; }
    public void setPalpitationDone(Boolean palpitationDone) { this.palpitationDone = palpitationDone; }

    public Boolean getReferToRoutineLabExams() { return referToRoutineLabExams; }
    public void setReferToRoutineLabExams(Boolean referToRoutineLabExams) { this.referToRoutineLabExams = referToRoutineLabExams; }

    public Boolean getAdvisedDiet() { return advisedDiet; }
    public void setAdvisedDiet(Boolean advisedDiet) { this.advisedDiet = advisedDiet; }

    public Boolean getAdvisedHydration() { return advisedHydration; }
    public void setAdvisedHydration(Boolean advisedHydration) { this.advisedHydration = advisedHydration; }

    public Boolean getAdvisedExercise() { return advisedExercise; }
    public void setAdvisedExercise(Boolean advisedExercise) { this.advisedExercise = advisedExercise; }

    public Boolean getHygieneCareEmphasized() { return hygieneCareEmphasized; }
    public void setHygieneCareEmphasized(Boolean hygieneCareEmphasized) { this.hygieneCareEmphasized = hygieneCareEmphasized; }

    public Boolean getRefrainFromHeavyObjects() { return refrainFromHeavyObjects; }
    public void setRefrainFromHeavyObjects(Boolean refrainFromHeavyObjects) { this.refrainFromHeavyObjects = refrainFromHeavyObjects; }

    public Boolean getAdvisedVitaminIntake() { return advisedVitaminIntake; }
    public void setAdvisedVitaminIntake(Boolean advisedVitaminIntake) { this.advisedVitaminIntake = advisedVitaminIntake; }

    public Boolean getDiscussedFacilityDeliveryImportance() { return discussedFacilityDeliveryImportance; }
    public void setDiscussedFacilityDeliveryImportance(Boolean discussedFacilityDeliveryImportance) { this.discussedFacilityDeliveryImportance = discussedFacilityDeliveryImportance; }

    public String getReferralDoctor() { return referralDoctor; }
    public void setReferralDoctor(String referralDoctor) { this.referralDoctor = referralDoctor; }

    public String getLabResultsWithTelephoneOrder() { return labResultsWithTelephoneOrder; }
    public void setLabResultsWithTelephoneOrder(String labResultsWithTelephoneOrder) { this.labResultsWithTelephoneOrder = labResultsWithTelephoneOrder; }

    public Date getTcbOn() { return tcbOn; }
    public void setTcbOn(Date tcbOn) { this.tcbOn = tcbOn; }
}