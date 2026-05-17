package com.backend.backend.service.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist;
import com.backend.backend.repository.FamilyPlanning.PregnancyExclusionChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PregnancyExclusionChecklistServiceImpl implements PregnancyExclusionChecklistService {

    @Autowired
    private PregnancyExclusionChecklistRepository repository;

    @Override
    public PregnancyExclusionChecklist add(PregnancyExclusionChecklist checklist) {
        return repository.save(checklist);
    }

    @Override
    public PregnancyExclusionChecklist getById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pregnancy Exclusion Checklist not found with id: " + id));
    }

    @Override
    public List<PregnancyExclusionChecklist> getAll() {
        return (List<PregnancyExclusionChecklist>) repository.findAll();
    }

    @Override
    public PregnancyExclusionChecklist update(PregnancyExclusionChecklist checklist) {
        return repository.save(checklist);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<PregnancyExclusionChecklist> getByClientID(Integer clientID) {
        return repository.findByClientID(clientID);
    }
}