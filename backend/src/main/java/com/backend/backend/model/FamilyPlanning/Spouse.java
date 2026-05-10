package com.backend.backend.model.FamilyPlanning;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Spouse")
public class Spouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spouseID")
    private Integer spouseID;

    @Column(name = "clientID", nullable = false)
    private Integer clientID;

    @Column(name = "lName", nullable = false, length = 50)
    private String lName;

    @Column(name = "fName", nullable = false, length = 50)
    private String fName;

    @Column(name = "midInitial", length = 5)
    private String midInitial;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "Age")
    private Integer age;

    @Column(name = "Occupation", length = 100)
    private String occupation;

    // Getters and Setters
    public Integer getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(Integer spouseID) {
        this.spouseID = spouseID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getMidInitial() {
        return midInitial;
    }

    public void setMidInitial(String midInitial) {
        this.midInitial = midInitial;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
