package com.backend.backend.service.Prenatal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.ConsultationVisits;
import com.backend.backend.repository.Prenatal.ConsultationVisitsRepository;

@Service
public class ConsultationVisitsServiceImpl implements ConsultationVisitsService {

    @Autowired
    private ConsultationVisitsRepository repository;

    @Override
    public ConsultationVisits add(ConsultationVisits visit) {
        return repository.save(visit);
    }

    @Override
    public ConsultationVisits getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ConsultationVisit not found: " + id));
    }

    @Override
    public List<ConsultationVisits> getAll() {
        return repository.findAll();
    }

    @Override
    public ConsultationVisits update(ConsultationVisits visit) {
        return repository.save(visit);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<ConsultationVisits> getByConsultationRecordID(Integer consultationRecordID) {
        return repository.findByConsultationRecordID(consultationRecordID);
    }
}
