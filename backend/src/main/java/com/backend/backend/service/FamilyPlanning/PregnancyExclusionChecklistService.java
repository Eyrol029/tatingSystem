package com.backend.backend.service.FamilyPlanning;

import java.util.List;

import com.backend.backend.model.FamilyPlanning.PregnancyExclusionChecklist;

public interface PregnancyExclusionChecklistService {

    PregnancyExclusionChecklist add(PregnancyExclusionChecklist checklist);

    PregnancyExclusionChecklist getById(Integer id);

    List<PregnancyExclusionChecklist> getAll();

    PregnancyExclusionChecklist update(PregnancyExclusionChecklist checklist);

    void delete(Integer id);

    List<PregnancyExclusionChecklist> getByClientID(Integer clientID);
}
