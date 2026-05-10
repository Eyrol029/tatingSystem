package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Conjunctiva;
import com.backend.backend.repository.FamilyPlanning.ConjunctivaRepository;

@Service
public class ConjunctivaServiceImpl implements ConjunctivaService {

    @Autowired
    private ConjunctivaRepository conjunctivaRepository;

    @Override
    public Conjunctiva addConjunctiva(Conjunctiva conjunctiva) {
        return conjunctivaRepository.save(conjunctiva);
    }

    @Override
    public Conjunctiva getConjunctivaById(Integer id) {
        return conjunctivaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conjunctiva not found: " + id));
    }

    @Override
    public List<Conjunctiva> getAllConjunctiva() {
        return conjunctivaRepository.findAll();
    }

    @Override
    public Conjunctiva updateConjunctiva(Conjunctiva conjunctiva) {
        return conjunctivaRepository.save(conjunctiva);
    }

    @Override
    public void deleteConjunctiva(Integer id) {
        conjunctivaRepository.deleteById(id);
    }

    @Override
    public List<Conjunctiva> getByPExamID(Integer pExamID) {
        return conjunctivaRepository.findByPExamID(pExamID);
    }
}
