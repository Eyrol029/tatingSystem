package com.backend.backend.repository.FamilyPlanning;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist;

public interface PregnancyExclusionChecklistRepository extends CrudRepository<PregnancyExclusionChecklist, Integer> {

    List<PregnancyExclusionChecklist> findAll();

    List<PregnancyExclusionChecklist> findByClientID(Integer clientID);
}
