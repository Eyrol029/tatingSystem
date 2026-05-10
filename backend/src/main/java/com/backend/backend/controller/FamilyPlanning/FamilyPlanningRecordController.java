package com.backend.backend.controller.FamilyPlanning;

import com.backend.backend.model.FamilyPlanning.FamilyPlanningRecord;
import com.backend.backend.service.FamilyPlanning.FamilyPlanningRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/familyplanning/records")
public class FamilyPlanningRecordController {

    @Autowired
    private FamilyPlanningRecordService familyPlanningRecordService;

    @PostMapping
    public FamilyPlanningRecord addRecord(@RequestBody FamilyPlanningRecord record) {
        return familyPlanningRecordService.addRecord(record);
    }

    @GetMapping("/{id}")
    public FamilyPlanningRecord getRecord(@PathVariable Integer id) {
        return familyPlanningRecordService.getRecordById(id);
    }

    @GetMapping
    public List<FamilyPlanningRecord> getRecords() {
        return familyPlanningRecordService.getRecords();
    }

    @PutMapping
    public FamilyPlanningRecord updateRecord(@RequestBody FamilyPlanningRecord record) {
        return familyPlanningRecordService.updateRecord(record);
    }

    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        familyPlanningRecordService.deleteRecord(id);
        return "FamilyPlanningRecord deleted.";
    }

    @GetMapping("/client/{clientID}")
    public List<FamilyPlanningRecord> getByClientID(@PathVariable Integer clientID) {
        return familyPlanningRecordService.getRecordsByClientID(clientID);
    }

    @GetMapping("/service/{serviceID}")
    public List<FamilyPlanningRecord> getByServiceID(@PathVariable Integer serviceID) {
        return familyPlanningRecordService.getRecordsByServiceID(serviceID);
    }
}
