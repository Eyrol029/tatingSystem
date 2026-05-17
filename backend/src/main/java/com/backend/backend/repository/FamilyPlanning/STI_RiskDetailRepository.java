package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.STI_RiskDetail;

public interface STI_RiskDetailRepository extends CrudRepository<STI_RiskDetail, Integer> {

    List<STI_RiskDetail> findAll();

    List<STI_RiskDetail> findByStiRiskID(Integer stiRiskID);

    List<STI_RiskDetail> findByStiConditionID(Integer stiConditionID);
}
