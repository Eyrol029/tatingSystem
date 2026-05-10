package com.backend.backend.service.familyplanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.familyplanning.STI_Condition;
import com.backend.backend.repository.familyplanning.STI_ConditionRepository;

@Service
public class STI_ConditionServiceImpl implements STI_ConditionService {

    @Autowired
    private STI_ConditionRepository repository;

    @Override
    public STI_Condition addCondition(STI_Condition condition) {
        return repository.save(condition);
    }

    @Override
    public STI_Condition getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("STI_Condition not found: " + id));
    }

    @Override
    public List<STI_Condition> getAll() {
        return repository.findAll();
    }

    @Override
    public STI_Condition update(STI_Condition condition) {
        return repository.save(condition);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
