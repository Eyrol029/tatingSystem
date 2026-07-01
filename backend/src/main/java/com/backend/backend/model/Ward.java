package com.backend.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String wardCode; // e.g., W001

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // Maternity, Recovery, Prenatal, Postnatal, ICU

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer occupied;

    @Column(nullable = false)
    private String status; // Available, Full, Maintenance

    @Column(nullable = false)
    private String floor; // Ground Floor, 1st Floor, 2nd Floor, 3rd Floor

    @ElementCollection
    @CollectionTable(name = "ward_facilities", joinColumns = @JoinColumn(name = "ward_id"))
    @Column(name = "facility")
    private List<String> facilities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getOccupied() {
        return occupied;
    }

    public void setOccupied(Integer occupied) {
        this.occupied = occupied;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }
}