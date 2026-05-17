package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.ObstetricalCondition;
import com.backend.backend.repository.FamilyPlanning.ObstetricalConditionRepository;

@Service
public class ObstetricalConditionServiceImpl implements ObstetricalConditionService {

    @Autowired
    private ObstetricalConditionRepository repository;

    @Override
    public ObstetricalCondition addCondition(ObstetricalCondition condition) {
        return repository.save(condition);
    }

    @Override
    public ObstetricalCondition getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ObstetricalCondition not found: " + id));
    }

    @Override
    public List<ObstetricalCondition> getAll() {
        return repository.findAll();
    }

    @Override
    public ObstetricalCondition update(ObstetricalCondition condition) {
        return repository.save(condition);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
