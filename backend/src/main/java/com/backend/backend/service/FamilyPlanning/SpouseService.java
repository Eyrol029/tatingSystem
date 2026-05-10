package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Spouse;

public interface SpouseService {

    Spouse addSpouse(Spouse spouse);

    Spouse getSpouseById(Integer id);

    List<Spouse> getSpouses();

    Spouse updateSpouse(Spouse spouse);

    void deleteSpouse(Integer id);

    List<Spouse> getSpousesByClientID(Integer clientID);

    List<Spouse> getSpousesByLName(String lName);
}
