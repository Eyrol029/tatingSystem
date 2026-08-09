package com.backend.backend.service.FamilyPlanning;

import java.util.List;
import java.util.Optional;

import com.backend.backend.model.FamilyPlanning.Acknowledgement;

public interface AcknowledgementService {
    Acknowledgement saveOrUpdate(Acknowledgement ack);
    List<Acknowledgement> getByClientID(Long clientID);
    Optional<Acknowledgement> getByServiceID(Long serviceID);
}
