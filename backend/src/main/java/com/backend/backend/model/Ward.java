package com.backend.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "wards")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}