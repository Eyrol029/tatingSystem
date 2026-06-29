package com.backend.backend.repository;

import com.backend.backend.model.ClinicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicalServiceRepository extends JpaRepository<ClinicalService, Long> {

    List<ClinicalService> findByCategory(String category);

    Optional<ClinicalService> findByServiceCode(String serviceCode);

    boolean existsByServiceCode(String serviceCode);
}