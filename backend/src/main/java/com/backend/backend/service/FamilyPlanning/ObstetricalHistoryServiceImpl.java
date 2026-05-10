package com.backend.backend.service.familyplanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.familyplanning.ObstetricalHistory;
import com.backend.backend.repository.familyplanning.ObstetricalHistoryRepository;

@Service
public class ObstetricalHistoryServiceImpl implements ObstetricalHistoryService {

    @Autowired
    private ObstetricalHistoryRepository repository;

    @Override
    public ObstetricalHistory addObstetricalHistory(ObstetricalHistory history) {
        return repository.save(history);
    }

    @Override
    public ObstetricalHistory getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ObstetricalHistory not found: " + id));
    }

    @Override
    public List<ObstetricalHistory> getAll() {
        return repository.findAll();
    }

    @Override
    public ObstetricalHistory update(ObstetricalHistory history) {
        return repository.save(history);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ObstetricalHistory> getByClientID(Integer clientID) {
        return repository.findByClientID(clientID);
    }
}
