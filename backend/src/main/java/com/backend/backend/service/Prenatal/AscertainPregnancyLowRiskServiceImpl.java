package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.AscertainPregnancyLowRisk;
import com.backend.backend.repository.Prenatal.AscertainPregnancyLowRiskRepository;

@Service
public class AscertainPregnancyLowRiskServiceImpl implements AscertainPregnancyLowRiskService {

    @Autowired
    private AscertainPregnancyLowRiskRepository repository;

    @Override
    public AscertainPregnancyLowRisk addRecord(AscertainPregnancyLowRisk record) {
        return repository.save(record);
    }

    @Override
    public AscertainPregnancyLowRisk getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("AscertainPregnancyLowRisk not found: " + id));
    }

    @Override
    public List<AscertainPregnancyLowRisk> getRecords() {
        return repository.findAll();
    }

    @Override
    public AscertainPregnancyLowRisk updateRecord(AscertainPregnancyLowRisk record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<AscertainPregnancyLowRisk> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}
