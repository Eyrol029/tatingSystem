package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clientID")
    private Integer clientID;

    @Column(name = "nhts")
    private String nhts;

    @Column(name = "is4PSMember")
    private String is4PSMember;

    @Column(name = "CivilStatus", length = 20)
    private String civilStatus;

    // Getters and Setters
    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getNhts() {
        return nhts;
    }

    public void setNhts(String nhts) {
        this.nhts = nhts;
    }

    public String getIs4PSMember() {
        return is4PSMember;
    }

    public void setIs4PSMember(String is4PSMember) {
        this.is4PSMember = is4PSMember;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }
}
