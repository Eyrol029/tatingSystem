package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Neck;
import com.backend.backend.repository.FamilyPlanning.NeckRepository;

@Service
public class NeckServiceImpl implements NeckService {

    @Autowired
    private NeckRepository neckRepository;

    @Override
    public Neck addNeck(Neck neck) {
        return neckRepository.save(neck);
    }

    @Override
    public Neck getNeckById(Integer id) {
        return neckRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Neck not found: " + id));
    }

    @Override
    public List<Neck> getNecks() {
        return neckRepository.findAll();
    }

    @Override
    public Neck updateNeck(Neck neck) {
        return neckRepository.save(neck);
    }

    @Override
    public void deleteNeck(Integer id) {
        neckRepository.deleteById(id);
    }

    @Override
    public List<Neck> getByPExamID(Integer pExamID) {
        return neckRepository.findByPExamID(pExamID);
    }
}
