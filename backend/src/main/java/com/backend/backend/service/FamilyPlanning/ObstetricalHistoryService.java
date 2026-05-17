package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.ObstetricalHistory;

public interface ObstetricalHistoryService {

    ObstetricalHistory addObstetricalHistory(ObstetricalHistory history);

    ObstetricalHistory getById(Integer id);

    List<ObstetricalHistory> getAll();

    ObstetricalHistory update(ObstetricalHistory history);

    void delete(Integer id);

    List<ObstetricalHistory> getByClientID(Integer clientID);
}
