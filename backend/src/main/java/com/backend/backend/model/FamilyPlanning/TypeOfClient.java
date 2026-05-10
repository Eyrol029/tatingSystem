package com.backend.backend.model.FamilyPlanning;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TypeOfClient")
public class TypeOfClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeID")
    private Integer typeID;

    @Column(name = "clientID", nullable = false)
    private Integer clientID;

    @Column(name = "IsNewAcceptor", nullable = false)
    private Boolean isNewAcceptor;

    @Column(name = "ReasonForFp", nullable = false, length = 50)
    private String reasonForFp;

    @Column(name = "ReasonOtherDetails", nullable = false, length = 255)
    private String reasonOtherDetails;

    @Column(name = "IsCurrentUser")
    private Boolean isCurrentUser;

    @Column(name = "IsChangingMethod")
    private Boolean isChangingMethod;

    @Column(name = "ChangeReason", length = 100)
    private String changeReason;

    @Column(name = "ChangeReasonOthers", length = 255)
    private String changeReasonOthers;

    @Column(name = "IsChangingClinic")
    private Boolean isChangingClinic;

    @Column(name = "IsDropOutRestart")
    private Boolean isDropOutRestart;

    // Getters and Setters
    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Boolean getIsNewAcceptor() {
        return isNewAcceptor;
    }

    public void setIsNewAcceptor(Boolean isNewAcceptor) {
        this.isNewAcceptor = isNewAcceptor;
    }

    public String getReasonForFp() {
        return reasonForFp;
    }

    public void setReasonForFp(String reasonForFp) {
        this.reasonForFp = reasonForFp;
    }

    public String getReasonOtherDetails() {
        return reasonOtherDetails;
    }

    public void setReasonOtherDetails(String reasonOtherDetails) {
        this.reasonOtherDetails = reasonOtherDetails;
    }

    public Boolean getIsCurrentUser() {
        return isCurrentUser;
    }

    public void setIsCurrentUser(Boolean isCurrentUser) {
        this.isCurrentUser = isCurrentUser;
    }

    public Boolean getIsChangingMethod() {
        return isChangingMethod;
    }

    public void setIsChangingMethod(Boolean isChangingMethod) {
        this.isChangingMethod = isChangingMethod;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeReasonOthers() {
        return changeReasonOthers;
    }

    public void setChangeReasonOthers(String changeReasonOthers) {
        this.changeReasonOthers = changeReasonOthers;
    }

    public Boolean getIsChangingClinic() {
        return isChangingClinic;
    }

    public void setIsChangingClinic(Boolean isChangingClinic) {
        this.isChangingClinic = isChangingClinic;
    }

    public Boolean getIsDropOutRestart() {
        return isDropOutRestart;
    }

    public void setIsDropOutRestart(Boolean isDropOutRestart) {
        this.isDropOutRestart = isDropOutRestart;
    }
}
