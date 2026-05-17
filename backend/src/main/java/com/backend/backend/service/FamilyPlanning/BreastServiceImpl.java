package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Breast;
import com.backend.backend.repository.FamilyPlanning.BreastRepository;

@Service
public class BreastServiceImpl implements BreastService {

    @Autowired
    private BreastRepository breastRepository;

    @Override
    public Breast addBreast(Breast breast) {
        return breastRepository.save(breast);
    }

    @Override
    public Breast getBreastById(Integer id) {
        return breastRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Breast not found: " + id));
    }

    @Override
    public List<Breast> getBreasts() {
        return breastRepository.findAll();
    }

    @Override
    public Breast updateBreast(Breast breast) {
        return breastRepository.save(breast);
    }

    @Override
    public void deleteBreast(Integer id) {
        breastRepository.deleteById(id);
    }

    @Override
    public List<Breast> getByPExamID(Integer pExamID) {
        return breastRepository.findBypExamID(pExamID);
    }
}
