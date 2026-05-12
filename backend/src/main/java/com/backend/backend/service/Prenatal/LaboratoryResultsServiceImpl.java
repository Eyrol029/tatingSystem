package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.LaboratoryResults;
import com.backend.backend.repository.Prenatal.LaboratoryResultsRepository;

@Service
public class LaboratoryResultsServiceImpl implements LaboratoryResultsService {

    @Autowired
    private LaboratoryResultsRepository repository;

    @Override
    public LaboratoryResults add(LaboratoryResults result) {
        return repository.save(result);
    }

    @Override
    public LaboratoryResults getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("LaboratoryResults not found: " + id));
    }

    @Override
    public List<LaboratoryResults> getAll() {
        return repository.findAll();
    }

    @Override
    public LaboratoryResults update(LaboratoryResults result) {
        return repository.save(result);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<LaboratoryResults> getByPrenatalRecordID(Integer prenatalRecordID) {
        return repository.findByPrenatalRecordID(prenatalRecordID);
    }
}
