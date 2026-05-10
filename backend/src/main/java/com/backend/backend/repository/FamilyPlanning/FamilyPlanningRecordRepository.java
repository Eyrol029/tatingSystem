package com.backend.backend.repository.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface FamilyPlanningRecordRepository extends CrudRepository<FamilyPlanningRecord, Integer> {

    List<FamilyPlanningRecord> findAll();

    List<FamilyPlanningRecord> findByClientID(Integer clientID);

    List<FamilyPlanningRecord> findByServiceID(Integer serviceID);
}
