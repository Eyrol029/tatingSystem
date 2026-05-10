package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Abdomen;
import com.backend.backend.repository.FamilyPlanning.AbdomenRepository;

@Service
public class AbdomenServiceImpl implements AbdomenService {

    @Autowired
    private AbdomenRepository abdomenRepository;

    @Override
    public Abdomen addAbdomen(Abdomen abdomen) {
        return abdomenRepository.save(abdomen);
    }

    @Override
    public Abdomen getAbdomenById(Integer id) {
        return abdomenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abdomen not found: " + id));
    }

    @Override
    public List<Abdomen> getAbdomens() {
        return abdomenRepository.findAll();
    }

    @Override
    public Abdomen updateAbdomen(Abdomen abdomen) {
        return abdomenRepository.save(abdomen);
    }

    @Override
    public void deleteAbdomen(Integer id) {
        abdomenRepository.deleteById(id);
    }

    @Override
    public List<Abdomen> getByPExamID(Integer pExamID) {
        return abdomenRepository.findByPExamID(pExamID);
    }
}
