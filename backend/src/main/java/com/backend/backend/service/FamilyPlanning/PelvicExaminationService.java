package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.PelvicExamination;

public interface PelvicExaminationService {

    PelvicExamination addPelvicExamination(PelvicExamination pelvic);

    PelvicExamination getPelvicExaminationById(Integer id);

    List<PelvicExamination> getPelvicExaminations();

    PelvicExamination updatePelvicExamination(PelvicExamination pelvic);

    void deletePelvicExamination(Integer id);

    List<PelvicExamination> getByPExamID(Integer pExamID);
}
