package com.backend.backend.service.familyplanning;

import java.util.List;

import com.backend.backend.model.familyplanning.ObstetricalHistory;

public interface ObstetricalHistoryService {

    ObstetricalHistory addObstetricalHistory(ObstetricalHistory history);

    ObstetricalHistory getById(Integer id);

    List<ObstetricalHistory> getAll();

    ObstetricalHistory update(ObstetricalHistory history);

    void delete(Integer id);

    List<ObstetricalHistory> getByClientID(Integer clientID);
}
