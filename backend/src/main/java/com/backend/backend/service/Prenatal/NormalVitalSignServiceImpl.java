package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.NormalVitalSign;
import com.backend.backend.repository.Prenatal.NormalVitalSignRepository;

@Service
public class NormalVitalSignServiceImpl implements NormalVitalSignService {

    @Autowired
    private NormalVitalSignRepository repository;

    @Override
    public NormalVitalSign addRecord(NormalVitalSign record) {
        return repository.save(record);
    }

    @Override
    public NormalVitalSign getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NormalVitalSign not found: " + id));
    }

    @Override
    public List<NormalVitalSign> getRecords() {
        return repository.findAll();
    }

    @Override
    public NormalVitalSign updateRecord(NormalVitalSign record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<NormalVitalSign> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}
