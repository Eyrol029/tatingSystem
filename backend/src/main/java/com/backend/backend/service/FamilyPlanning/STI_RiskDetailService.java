package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.STI_RiskDetail;

public interface STI_RiskDetailService {

    STI_RiskDetail addDetail(STI_RiskDetail detail);

    STI_RiskDetail getById(Integer id);

    List<STI_RiskDetail> getAll();

    STI_RiskDetail update(STI_RiskDetail detail);

    void delete(Integer id);

    List<STI_RiskDetail> getByRiskID(Integer stiRiskID);

    List<STI_RiskDetail> getByConditionID(Integer stiConditionID);
}
