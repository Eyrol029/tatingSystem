package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.ObstetricRiskFactor;
import com.backend.backend.repository.Prenatal.ObstetricRiskFactorRepository;

@Service
public class ObstetricRiskFactorServiceImpl implements ObstetricRiskFactorService {

    @Autowired
    private ObstetricRiskFactorRepository repository;

    @Override
    public ObstetricRiskFactor add(ObstetricRiskFactor data) {
        return repository.save(data);
    }

    @Override
    public ObstetricRiskFactor getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ObstetricRiskFactor not found: " + id));
    }

    @Override
    public List<ObstetricRiskFactor> getAll() {
        return repository.findAll();
    }

    @Override
    public ObstetricRiskFactor update(ObstetricRiskFactor data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ObstetricRiskFactor> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}
