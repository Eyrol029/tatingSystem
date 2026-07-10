package com.backend.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CalendarEvent")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventID")
    private Integer eventID;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "eventDate", nullable = false)
    private LocalDate eventDate;

    @Column(name = "eventType", length = 50)
    private String eventType;

    @Column(name = "patientName", length = 200)
    private String patientName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "patientID")
    private Integer patientID;

    public Integer getPatientID() {
        return patientID;
    }

public void setPatientID(Integer patientID) {
    this.patientID = patientID;
}
}