package com.backend.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backend.backend.model.Patient;
import com.backend.backend.service.PatientService;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;
 @CrossOrigin(origins = "*")
    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

        @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Integer id) {
        return patientService.getPatientById(id);
    }
        @CrossOrigin(origins = "*")
    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }
        @CrossOrigin(origins = "*")
    @PutMapping
    public Patient updatePatient(@RequestBody Patient patient) {
        return patientService.updatePatient(patient);
    }

        @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return "Patient deleted.";
    }
        @CrossOrigin(origins = "*")
    @GetMapping("/search/lastname/{lName}")
    public List<Patient> getByLastName(@PathVariable String lName) {
        return patientService.getPatientsByLastName(lName);
    }
        @CrossOrigin(origins = "*")
    @GetMapping("/search/barangay/{barangay}")
    public List<Patient> getByBarangay(@PathVariable String barangay) {
        return patientService.getPatientsByBarangay(barangay);
    }
}