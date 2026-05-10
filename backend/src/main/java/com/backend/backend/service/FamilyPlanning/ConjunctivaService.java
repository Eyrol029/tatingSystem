package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.Conjunctiva;

public interface ConjunctivaService {

    Conjunctiva addConjunctiva(Conjunctiva conjunctiva);

    Conjunctiva getConjunctivaById(Integer id);

    List<Conjunctiva> getAllConjunctiva();

    Conjunctiva updateConjunctiva(Conjunctiva conjunctiva);

    void deleteConjunctiva(Integer id);

    List<Conjunctiva> getByPExamID(Integer pExamID);
}
