package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;
import com.backend.backend.repository.Prenatal.ClinicalHistoryAndPhysicalExaminationRepository;

@Service
public class ClinicalHistoryAndPhysicalExaminationServiceImpl
        implements ClinicalHistoryAndPhysicalExaminationService {

    @Autowired
    private ClinicalHistoryAndPhysicalExaminationRepository repository;

    @Override
    public ClinicalHistoryAndPhysicalExamination addRecord(
            ClinicalHistoryAndPhysicalExamination record) {
        return repository.save(record);
    }

    @Override
    public ClinicalHistoryAndPhysicalExamination getRecordById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                "ClinicalHistoryAndPhysicalExamination not found: " + id));
    }

    @Override
    public List<ClinicalHistoryAndPhysicalExamination> getRecords() {
        return repository.findAll();
    }

    @Override
    public ClinicalHistoryAndPhysicalExamination updateRecord(
            ClinicalHistoryAndPhysicalExamination record) {
        return repository.save(record);
    }

    @Override
    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ClinicalHistoryAndPhysicalExamination> getByPrenatalrecordID(
            Integer prenatalrecordID) {
        return repository.findByPrenatalrecordID(prenatalrecordID);
    }
}
