package com.backend.backend.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appointmentID;

    private Integer patientID;
    private Integer patientServiceID;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private String fName;
    private String lName;
    private String middleI;
    private Integer age;

    private String address;
    private String serviceType;

    private Integer gravida;
    private Integer term;
    private Integer preterm;
    private Integer noOfLivingChildren;

    // getters and setters
    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getPatientServiceID() {
        return patientServiceID;
    }

    public void setPatientServiceID(Integer patientServiceID) {
        this.patientServiceID = patientServiceID;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getMiddleI() {
        return middleI;
    }

    public void setMiddleI(String middleI) {
        this.middleI = middleI;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getGravida() {
        return gravida;
    }

    public void setGravida(Integer gravida) {
        this.gravida = gravida;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getPreterm() {
        return preterm;
    }

    public void setPreterm(Integer preterm) {
        this.preterm = preterm;
    }

    public Integer getNoOfLivingChildren() {
        return noOfLivingChildren;
    }

    public void setNoOfLivingChildren(Integer noOfLivingChildren) {
        this.noOfLivingChildren = noOfLivingChildren;
    }
}
