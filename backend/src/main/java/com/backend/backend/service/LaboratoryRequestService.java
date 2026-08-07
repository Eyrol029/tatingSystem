package com.backend.backend.service;

import java.util.List;
import java.util.Optional;

import com.backend.backend.model.LaboratoryRequest;

public interface LaboratoryRequestService {

    List<LaboratoryRequest> getAllRequests();

    Optional<LaboratoryRequest> getRequestById(Long id);

    LaboratoryRequest createRequest(LaboratoryRequest request);

    List<LaboratoryRequest> searchByPatientName(String name);

    void deleteRequest(Long id);
}
