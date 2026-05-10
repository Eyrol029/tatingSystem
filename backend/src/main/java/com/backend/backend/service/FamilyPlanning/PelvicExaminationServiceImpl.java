package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.PelvicExamination;
import com.backend.backend.repository.FamilyPlanning.PelvicExaminationRepository;

@Service
public class PelvicExaminationServiceImpl implements PelvicExaminationService {

    @Autowired
    private PelvicExaminationRepository pelvicExaminationRepository;

    @Override
    public PelvicExamination addPelvicExamination(PelvicExamination pelvic) {
        return pelvicExaminationRepository.save(pelvic);
    }

    @Override
    public PelvicExamination getPelvicExaminationById(Integer id) {
        return pelvicExaminationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PelvicExamination not found: " + id));
    }

    @Override
    public List<PelvicExamination> getPelvicExaminations() {
        return pelvicExaminationRepository.findAll();
    }

    @Override
    public PelvicExamination updatePelvicExamination(PelvicExamination pelvic) {
        return pelvicExaminationRepository.save(pelvic);
    }

    @Override
    public void deletePelvicExamination(Integer id) {
        pelvicExaminationRepository.deleteById(id);
    }

    @Override
    public List<PelvicExamination> getByPExamID(Integer pExamID) {
        return pelvicExaminationRepository.findByPExamID(pExamID);
    }
}
