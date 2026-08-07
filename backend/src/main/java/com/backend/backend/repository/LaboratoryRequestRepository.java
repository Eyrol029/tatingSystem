package com.backend.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.LaboratoryRequest;

@Repository
public interface LaboratoryRequestRepository extends JpaRepository<LaboratoryRequest, Long> {
    
    // Optional query method to search by patient name
    List<LaboratoryRequest> findByPatientNameContainingIgnoreCase(String patientName);
}