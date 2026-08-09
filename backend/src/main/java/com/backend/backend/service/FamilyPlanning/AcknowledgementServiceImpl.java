package com.backend.backend.service.FamilyPlanning;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Acknowledgement;
import com.backend.backend.repository.FamilyPlanning.AcknowledgementRepository;

@Service
public class AcknowledgementServiceImpl implements AcknowledgementService {

    private final AcknowledgementRepository acknowledgementRepository;

    public AcknowledgementServiceImpl(AcknowledgementRepository acknowledgementRepository) {
        this.acknowledgementRepository = acknowledgementRepository;
    }

    @Override
    public Acknowledgement saveOrUpdate(Acknowledgement ack) {
        return acknowledgementRepository.save(ack);
    }

    @Override
    public List<Acknowledgement> getByClientID(Long clientID) {
        return acknowledgementRepository.findByClientID(clientID);
    }

    @Override
    public Optional<Acknowledgement> getByServiceID(Long serviceID) {
        // Updated method name
        return acknowledgementRepository.findFirstByServiceIDOrderByAcknowledgementIdDesc(serviceID);
    }
}
