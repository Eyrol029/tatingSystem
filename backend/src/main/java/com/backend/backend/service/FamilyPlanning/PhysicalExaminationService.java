package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.PhysicalExamination;

public interface PhysicalExaminationService {

    PhysicalExamination addExam(PhysicalExamination exam);

    PhysicalExamination getExamById(Integer id);

    List<PhysicalExamination> getAllExams();

    PhysicalExamination updateExam(PhysicalExamination exam);

    void deleteExam(Integer id);

    List<PhysicalExamination> getByClientID(Integer clientID);
}
