package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements;
import com.backend.backend.repository.Prenatal.MedicalSurgicalFactorMeasurementsRepository;

@Service
public class MedicalSurgicalFactorMeasurementsServiceImpl
        implements MedicalSurgicalFactorMeasurementsService {

    @Autowired
    private MedicalSurgicalFactorMeasurementsRepository repository;

    @Override
    public MedicalSurgicalFactorMeasurements add(MedicalSurgicalFactorMeasurements data) {
        return repository.save(data);
    }

    @Override
    public MedicalSurgicalFactorMeasurements getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found: " + id));
    }

    @Override
    public List<MedicalSurgicalFactorMeasurements> getAll() {
        return repository.findAll();
    }

    @Override
    public MedicalSurgicalFactorMeasurements update(MedicalSurgicalFactorMeasurements data) {
        return repository.save(data);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<MedicalSurgicalFactorMeasurements> getByMedsurgID(Integer medsurgID) {
        return repository.findByMedsurgID(medsurgID);
    }
}
