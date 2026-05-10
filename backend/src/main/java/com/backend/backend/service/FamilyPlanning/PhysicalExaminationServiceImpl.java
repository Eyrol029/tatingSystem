package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.PhysicalExamination;
import com.backend.backend.repository.FamilyPlanning.PhysicalExaminationRepository;

@Service
public class PhysicalExaminationServiceImpl implements PhysicalExaminationService {

    @Autowired
    private PhysicalExaminationRepository physicalExaminationRepository;

    @Override
    public PhysicalExamination addExam(PhysicalExamination exam) {
        return physicalExaminationRepository.save(exam);
    }

    @Override
    public PhysicalExamination getExamById(Integer id) {
        return physicalExaminationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhysicalExamination not found: " + id));
    }

    @Override
    public List<PhysicalExamination> getAllExams() {
        return physicalExaminationRepository.findAll();
    }

    @Override
    public PhysicalExamination updateExam(PhysicalExamination exam) {
        return physicalExaminationRepository.save(exam);
    }

    @Override
    public void deleteExam(Integer id) {
        physicalExaminationRepository.deleteById(id);
    }

    @Override
    public List<PhysicalExamination> getByClientID(Integer clientID) {
        return physicalExaminationRepository.findByClientID(clientID);
    }
}
