package com.backend.backend.service.Postpartum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.model.Postpartum.PostpartumCareRecord;
import com.backend.backend.repository.Postpartum.PostpartumCareRepository;

@Service
@Transactional
public class PostpartumCareServiceImpl implements PostpartumCareService {

    private final PostpartumCareRepository repository;

    @Autowired
    public PostpartumCareServiceImpl(PostpartumCareRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostpartumCareRecord> getAllRecords() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PostpartumCareRecord getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Postpartum Care record not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostpartumCareRecord> getRecordsByServiceID(Long serviceID) {
        return repository.findByServiceID(serviceID);
    }

    @Override
    public PostpartumCareRecord createRecord(PostpartumCareRecord record) {
        if (record.getServiceID() == null) {
            throw new IllegalArgumentException("Service ID cannot be null");
        }
        
        // Link child visits to the parent record
        if (record.getVisits() != null) {
            record.getVisits().forEach(visit -> visit.setPostpartumCareRecord(record));
        }

        return repository.save(record);
    }

    @Override
    public PostpartumCareRecord updateRecord(Long id, PostpartumCareRecord updatedRecord) {
        PostpartumCareRecord existing = getRecordById(id);

        existing.setPanNumber(updatedRecord.getPanNumber());
        existing.setHealthFacilityName(updatedRecord.getHealthFacilityName());
        existing.setLastName(updatedRecord.getLastName());
        existing.setFirstName(updatedRecord.getFirstName());
        existing.setMiddleName(updatedRecord.getMiddleName());
        existing.setPin(updatedRecord.getPin());
        existing.setHouseNumberStreet(updatedRecord.getHouseNumberStreet());
        existing.setSubdivisionBarangay(updatedRecord.getSubdivisionBarangay());
        existing.setCityMunicipality(updatedRecord.getCityMunicipality());
        existing.setProvince(updatedRecord.getProvince());
        existing.setZipCode(updatedRecord.getZipCode());

        existing.setGravida(updatedRecord.getGravida());
        existing.setParaTerm(updatedRecord.getParaTerm());
        existing.setParaPreterm(updatedRecord.getParaPreterm());
        existing.setParaAbortion(updatedRecord.getParaAbortion());
        existing.setParaLiving(updatedRecord.getParaLiving());
        existing.setDateOfDelivery(updatedRecord.getDateOfDelivery());
        existing.setBriefHistory(updatedRecord.getBriefHistory());

        existing.setReferred(updatedRecord.getReferred());
        existing.setReferredReason(updatedRecord.getReferredReason());
        existing.setAttendingStaffID(updatedRecord.getAttendingStaffID());
        existing.setAttendingSignatureName(updatedRecord.getAttendingSignatureName());
        existing.setPatientConformeName(updatedRecord.getPatientConformeName());
        existing.setDateSigned(updatedRecord.getDateSigned());

        // Clear and replace visits
        existing.getVisits().clear();
        if (updatedRecord.getVisits() != null) {
            updatedRecord.getVisits().forEach(visit -> existing.addVisit(visit));
        }

        return repository.save(existing);
    }

    @Override
    public void deleteRecord(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Postpartum Care record not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}