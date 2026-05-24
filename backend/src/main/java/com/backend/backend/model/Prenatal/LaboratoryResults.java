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

    // Getters and Setters
    public Integer getLaboratoryResultID() { return laboratoryResultID; }
    public void setLaboratoryResultID(Integer laboratoryResultID) { this.laboratoryResultID = laboratoryResultID; }

    public Integer getPrenatalRecordID() { return prenatalRecordID; }
    public void setPrenatalRecordID(Integer prenatalRecordID) { this.prenatalRecordID = prenatalRecordID; }

    public String getUrinalysis() { return urinalysis; }
    public void setUrinalysis(String urinalysis) { this.urinalysis = urinalysis; }

    public String getPusCells() { return pusCells; }
    public void setPusCells(String pusCells) { this.pusCells = pusCells; }

    public String getRedBloodCells() { return redBloodCells; }
    public void setRedBloodCells(String redBloodCells) { this.redBloodCells = redBloodCells; }

    public String getCompleteBloodCount() { return completeBloodCount; }
    public void setCompleteBloodCount(String completeBloodCount) { this.completeBloodCount = completeBloodCount; }

    public BigDecimal getHemoglobin() { return hemoglobin; }
    public void setHemoglobin(BigDecimal hemoglobin) { this.hemoglobin = hemoglobin; }

    public String getHepatitisBSurfaceAntigen() { return hepatitisBSurfaceAntigen; }
    public void setHepatitisBSurfaceAntigen(String hepatitisBSurfaceAntigen) { this.hepatitisBSurfaceAntigen = hepatitisBSurfaceAntigen; }

    public String getVenerealDiseaseResearchLaboratoryTest() { return venerealDiseaseResearchLaboratoryTest; }
    public void setVenerealDiseaseResearchLaboratoryTest(String v) { this.venerealDiseaseResearchLaboratoryTest = v; }

    public String getHumanImmunodeficiencyVirusTest() { return humanImmunodeficiencyVirusTest; }
    public void setHumanImmunodeficiencyVirusTest(String humanImmunodeficiencyVirusTest) { this.humanImmunodeficiencyVirusTest = humanImmunodeficiencyVirusTest; }

    public String getUltrasoundResult() { return ultrasoundResult; }
    public void setUltrasoundResult(String ultrasoundResult) { this.ultrasoundResult = ultrasoundResult; }

    public String getTetanusToxoid() { return tetanusToxoid; }
    public void setTetanusToxoid(String tetanusToxoid) { this.tetanusToxoid = tetanusToxoid; }
}