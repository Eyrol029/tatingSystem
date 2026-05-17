package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.STI_Risk;

public interface STI_RiskService {

    STI_Risk addRisk(STI_Risk risk);

    STI_Risk getById(Integer id);

    List<STI_Risk> getAll();

    STI_Risk update(STI_Risk risk);

    void delete(Integer id);

    List<STI_Risk> getByClientID(Integer clientID);
}
