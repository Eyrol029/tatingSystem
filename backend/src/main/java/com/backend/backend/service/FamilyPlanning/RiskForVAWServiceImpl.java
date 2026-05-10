package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.RiskForVAW;
import com.backend.backend.repository.FamilyPlanning.RiskForVAWRepository;

@Service
public class RiskForVAWServiceImpl implements RiskForVAWService {

    @Autowired
    private RiskForVAWRepository riskForVAWRepository;

    @Override
    public RiskForVAW addRisk(RiskForVAW risk) {
        return riskForVAWRepository.save(risk);
    }

    @Override
    public RiskForVAW getRiskById(Integer id) {
        return riskForVAWRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RiskForVAW not found: " + id));
    }

    @Override
    public List<RiskForVAW> getAllRisks() {
        return riskForVAWRepository.findAll();
    }

    @Override
    public RiskForVAW updateRisk(RiskForVAW risk) {
        return riskForVAWRepository.save(risk);
    }

    @Override
    public void deleteRisk(Integer id) {
        riskForVAWRepository.deleteById(id);
    }

    @Override
    public List<RiskForVAW> getByClientID(Integer clientID) {
        return riskForVAWRepository.findByClientID(clientID);
    }
}
