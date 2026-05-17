package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Extremities;
import com.backend.backend.repository.FamilyPlanning.ExtremitiesRepository;

@Service
public class ExtremitiesServiceImpl implements ExtremitiesService {

    @Autowired
    private ExtremitiesRepository extremitiesRepository;

    @Override
    public Extremities addExtremities(Extremities extremities) {
        return extremitiesRepository.save(extremities);
    }

    @Override
    public Extremities getExtremitiesById(Integer id) {
        return extremitiesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Extremities not found: " + id));
    }

    @Override
    public List<Extremities> getExtremities() {
        return extremitiesRepository.findAll();
    }

    @Override
    public Extremities updateExtremities(Extremities extremities) {
        return extremitiesRepository.save(extremities);
    }

    @Override
    public void deleteExtremities(Integer id) {
        extremitiesRepository.deleteById(id);
    }

    @Override
    public List<Extremities> getByPExamID(Integer pExamID) {
        return extremitiesRepository.findBypExamID(pExamID);
    }
}
