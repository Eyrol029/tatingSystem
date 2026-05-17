package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.ObstetricalConditionDetail;
import com.backend.backend.repository.FamilyPlanning.ObstetricalConditionDetailRepository;

@Service
public class ObstetricalConditionDetailServiceImpl implements ObstetricalConditionDetailService {

    @Autowired
    private ObstetricalConditionDetailRepository repository;

    @Override
    public ObstetricalConditionDetail addDetail(ObstetricalConditionDetail detail) {
        return repository.save(detail);
    }

    @Override
    public ObstetricalConditionDetail getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detail not found: " + id));
    }

    @Override
    public List<ObstetricalConditionDetail> getAll() {
        return repository.findAll();
    }

    @Override
    public ObstetricalConditionDetail update(ObstetricalConditionDetail detail) {
        return repository.save(detail);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ObstetricalConditionDetail> getByHistoryID(Integer obstetricalHistoryID) {
        return repository.findByObstetricalHistoryID(obstetricalHistoryID);
    }
}
