package com.backend.backend.model.familyplanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ObstetricalConditionDetail")
public class ObstetricalConditionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conditionDetailID")
    private Integer conditionDetailID;

    @Column(name = "obstetricalHistoryID")
    private Integer obstetricalHistoryID;

    @Column(name = "obstetricConditionID")
    private Integer obstetricConditionID;

    // Getters and Setters
    public Integer getConditionDetailID() {
        return conditionDetailID;
    }

    public void setConditionDetailID(Integer conditionDetailID) {
        this.conditionDetailID = conditionDetailID;
    }

    public Integer getObstetricalHistoryID() {
        return obstetricalHistoryID;
    }

    public void setObstetricalHistoryID(Integer obstetricalHistoryID) {
        this.obstetricalHistoryID = obstetricalHistoryID;
    }

    public Integer getObstetricConditionID() {
        return obstetricConditionID;
    }

    public void setObstetricConditionID(Integer obstetricConditionID) {
        this.obstetricConditionID = obstetricConditionID;
    }
}
