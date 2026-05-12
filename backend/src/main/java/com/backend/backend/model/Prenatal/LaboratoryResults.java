package com.backend.backend.model.Prenatal;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LaboratoryResults")
public class LaboratoryResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laboratoryResultID")
    private Integer laboratoryResultID;

    @Column(name = "prenatalRecordID")
    private Integer prenatalRecordID;

    @Column(name = "urinalysis")
    private String urinalysis;

    @Column(name = "pusCells")
    private String pusCells;

    @Column(name = "redBloodCells")
    private String redBloodCells;

    @Column(name = "completeBloodCount")
    private String completeBloodCount;

    @Column(name = "hemoglobin")
    private BigDecimal hemoglobin;

    @Column(name = "hepatitisBSurfaceAntigen")
    private String hepatitisBSurfaceAntigen;

    @Column(name = "venerealDiseaseResearchLaboratoryTest")
    private String venerealDiseaseResearchLaboratoryTest;

    @Column(name = "humanImmunodeficiencyVirusTest")
    private String humanImmunodeficiencyVirusTest;

    @Column(name = "ultrasoundResult")
    private String ultrasoundResult;

    @Column(name = "tetanusToxoid")
    private String tetanusToxoid;

    // getters and setters
}
