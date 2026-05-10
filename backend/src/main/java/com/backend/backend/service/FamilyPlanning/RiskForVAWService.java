package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.RiskForVAW;

public interface RiskForVAWService {

    RiskForVAW addRisk(RiskForVAW risk);

    RiskForVAW getRiskById(Integer id);

    List<RiskForVAW> getAllRisks();

    RiskForVAW updateRisk(RiskForVAW risk);

    void deleteRisk(Integer id);

    List<RiskForVAW> getByClientID(Integer clientID);
}
