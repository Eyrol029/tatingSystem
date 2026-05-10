package com.backend.backend.service.familyplanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.familyplanning.STI_RiskDetail;
import com.backend.backend.repository.familyplanning.STI_RiskDetailRepository;

@Service
public class STI_RiskDetailServiceImpl implements STI_RiskDetailService {

    @Autowired
    private STI_RiskDetailRepository repository;

    @Override
    public STI_RiskDetail addDetail(STI_RiskDetail detail) {
        return repository.save(detail);
    }

    @Override
    public STI_RiskDetail getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("STI_RiskDetail not found: " + id));
    }

    @Override
    public List<STI_RiskDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public STI_RiskDetail update(STI_RiskDetail detail) {
        return repository.save(detail);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<STI_RiskDetail> getByRiskID(Integer stiRiskID) {
        return repository.findByStiRiskID(stiRiskID);
    }

    @Override
    public List<STI_RiskDetail> getByConditionID(Integer stiConditionID) {
        return repository.findByStiConditionID(stiConditionID);
    }
}
