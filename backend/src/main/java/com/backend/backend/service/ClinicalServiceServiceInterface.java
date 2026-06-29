package com.backend.backend.service;

import com.backend.backend.model.ClinicalService;

import java.util.List;
import java.util.Optional;

public interface ClinicalServiceServiceInterface {

    List<ClinicalService> getAllServices();

    Optional<ClinicalService> getServiceById(Long id);

    List<ClinicalService> getServicesByCategory(String category);

    ClinicalService createService(ClinicalService service);

    Optional<ClinicalService> updateService(Long id, ClinicalService updatedService);

    boolean deleteService(Long id);
}