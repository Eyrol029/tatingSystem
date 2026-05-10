package com.backend.backend.model.FamilyPlanning;

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
@Table(name = "PregnancyExclusionChecklist")
public class PregnancyExclusionChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklistID")
    private Integer checklistID;

    @Column(name = "clientID", nullable = false)
    private Integer clientID;

    @Column(name = "dateRecorded")
    @Temporal(TemporalType.DATE)
    private Date dateRecorded;

    @Column(name = "question1")
    private Boolean question1;

    @Column(name = "question2")
    private Boolean question2;

    @Column(name = "question3")
    private Boolean question3;

    @Column(name = "question4")
    private Boolean question4;

    @Column(name = "question5")
    private Boolean question5;

    @Column(name = "question6")
    private Boolean question6;

    // GETTERS AND SETTERS
    public Integer getChecklistID() {
        return checklistID;
    }

    public void setChecklistID(Integer checklistID) {
        this.checklistID = checklistID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Date getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Date dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public Boolean getQuestion1() {
        return question1;
    }

    public void setQuestion1(Boolean question1) {
        this.question1 = question1;
    }

    public Boolean getQuestion2() {
        return question2;
    }

    public void setQuestion2(Boolean question2) {
        this.question2 = question2;
    }

    public Boolean getQuestion3() {
        return question3;
    }

    public void setQuestion3(Boolean question3) {
        this.question3 = question3;
    }

    public Boolean getQuestion4() {
        return question4;
    }

    public void setQuestion4(Boolean question4) {
        this.question4 = question4;
    }

    public Boolean getQuestion5() {
        return question5;
    }

    public void setQuestion5(Boolean question5) {
        this.question5 = question5;
    }

    public Boolean getQuestion6() {
        return question6;
    }

    public void setQuestion6(Boolean question6) {
        this.question6 = question6;
    }
}
