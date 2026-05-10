package com.backend.backend.service.familyplanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.familyplanning.STI_Risk;
import com.backend.backend.repository.familyplanning.STI_RiskRepository;

@Service
public class STI_RiskServiceImpl implements STI_RiskService {

    @Autowired
    private STI_RiskRepository repository;

    @Override
    public STI_Risk addRisk(STI_Risk risk) {
        return repository.save(risk);
    }

    @Override
    public STI_Risk getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("STI_Risk not found: " + id));
    }

    @Override
    public List<STI_Risk> getAll() {
        return repository.findAll();
    }

    @Override
    public STI_Risk update(STI_Risk risk) {
        return repository.save(risk);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<STI_Risk> getByClientID(Integer clientID) {
        return repository.findByClientID(clientID);
    }
}
