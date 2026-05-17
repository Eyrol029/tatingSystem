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
    @CrossOrigin(origins = "*")
    @PostMapping
    public FamilyPlanningRecord addRecord(@RequestBody FamilyPlanningRecord record) {
        return familyPlanningRecordService.addRecord(record);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public FamilyPlanningRecord getRecord(@PathVariable Integer id) {
        return familyPlanningRecordService.getRecordById(id);
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public List<FamilyPlanningRecord> getRecords() {
        return familyPlanningRecordService.getRecords();
    }
    @CrossOrigin(origins = "*")
    @PutMapping
    public FamilyPlanningRecord updateRecord(@RequestBody FamilyPlanningRecord record) {
        return familyPlanningRecordService.updateRecord(record);
    }
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Integer id) {
        familyPlanningRecordService.deleteRecord(id);
        return "FamilyPlanningRecord deleted.";
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/client/{clientID}")
    public List<FamilyPlanningRecord> getByClientID(@PathVariable Integer clientID) {
        return familyPlanningRecordService.getRecordsByClientID(clientID);
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/service/{serviceID}")
    public List<FamilyPlanningRecord> getByServiceID(@PathVariable Integer serviceID) {
        return familyPlanningRecordService.getRecordsByServiceID(serviceID);
    }
}
