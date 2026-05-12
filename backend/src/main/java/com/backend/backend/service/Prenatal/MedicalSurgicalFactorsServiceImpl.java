package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactors;
import com.backend.backend.repository.Prenatal.MedicalSurgicalFactorsRepository;

@Service
public class MedicalSurgicalFactorsServiceImpl implements MedicalSurgicalFactorsService {

    @Autowired
    private MedicalSurgicalFactorsRepository repository;

    @Override
    public MedicalSurgicalFactors add(MedicalSurgicalFactors data) {
        return repository.save(data);
    }

    @Override
    public MedicalSurgicalFactors getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MedicalSurgicalFactors not found: " + id));
    }

    @Override
    public List<MedicalSurgicalFactors> getAll() {
        return repository.findAll();
    }

    @Override
    public MedicalSurgicalFactors update(MedicalSurgicalFactors data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MedicalSurgicalFactors> getByClinicalHistoryID(Integer clinicalHistoryID) {
        return repository.findByClinicalHistoryID(clinicalHistoryID);
    }
}
