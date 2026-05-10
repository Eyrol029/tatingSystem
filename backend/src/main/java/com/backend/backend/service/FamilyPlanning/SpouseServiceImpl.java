package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.FamilyPlanning.Spouse;
import com.backend.backend.repository.FamilyPlanning.SpouseRepository;

@Service
public class SpouseServiceImpl implements SpouseService {

    @Autowired
    private SpouseRepository spouseRepository;

    @Override
    public Spouse addSpouse(Spouse spouse) {
        return spouseRepository.save(spouse);
    }

    @Override
    public Spouse getSpouseById(Integer id) {
        return spouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spouse not found: " + id));
    }

    @Override
    public List<Spouse> getSpouses() {
        return spouseRepository.findAll();
    }

    @Override
    public Spouse updateSpouse(Spouse spouse) {
        return spouseRepository.save(spouse);
    }

    @Override
    public void deleteSpouse(Integer id) {
        spouseRepository.deleteById(id);
    }

    @Override
    public List<Spouse> getSpousesByClientID(Integer clientID) {
        return spouseRepository.findByClientID(clientID);
    }

    @Override
    public List<Spouse> getSpousesByLName(String lName) {
        return spouseRepository.findByLName(lName);
    }
}
