package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Skin;
import com.backend.backend.repository.FamilyPlanning.SkinRepository;

@Service
public class SkinServiceImpl implements SkinService {

    @Autowired
    private SkinRepository skinRepository;

    @Override
    public Skin addSkin(Skin skin) {
        return skinRepository.save(skin);
    }

    @Override
    public Skin getSkinById(Integer id) {
        return skinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skin not found: " + id));
    }

    @Override
    public List<Skin> getAllSkin() {
        return skinRepository.findAll();
    }

    @Override
    public Skin updateSkin(Skin skin) {
        return skinRepository.save(skin);
    }

    @Override
    public void deleteSkin(Integer id) {
        skinRepository.deleteById(id);
    }

    @Override
    public List<Skin> getByPExamID(Integer pExamID) {
        return skinRepository.findBypExamID(pExamID);
    }
}
