package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.CompleteDiagnosis;
import com.backend.backend.repository.Prenatal.CompleteDiagnosisRepository;

@Service
public class CompleteDiagnosisServiceImpl implements CompleteDiagnosisService {

    @Autowired
    private CompleteDiagnosisRepository repository;

    @Override
    public CompleteDiagnosis add(CompleteDiagnosis data) {
        return repository.save(data);
    }

    @Override
    public CompleteDiagnosis getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found: " + id));
    }

    @Override
    public List<CompleteDiagnosis> getAll() {
        return repository.findAll();
    }

    @Override
    public CompleteDiagnosis update(CompleteDiagnosis data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<CompleteDiagnosis> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}
