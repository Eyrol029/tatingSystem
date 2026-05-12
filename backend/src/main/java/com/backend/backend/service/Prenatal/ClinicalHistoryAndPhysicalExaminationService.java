package com.backend.backend.service.Prenatal;

import java.util.List;

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;

public interface ClinicalHistoryAndPhysicalExaminationService {

    ClinicalHistoryAndPhysicalExamination addRecord(
            ClinicalHistoryAndPhysicalExamination record);

    ClinicalHistoryAndPhysicalExamination getRecordById(Integer id);

    List<ClinicalHistoryAndPhysicalExamination> getRecords();

    ClinicalHistoryAndPhysicalExamination updateRecord(
            ClinicalHistoryAndPhysicalExamination record);

    void deleteRecord(Integer id);

    List<ClinicalHistoryAndPhysicalExamination> getByPrenatalrecordID(
            Integer prenatalrecordID);
}
