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

    // getters and setters
}
