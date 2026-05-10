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

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.addPatient(patient);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Integer id) {
        return patientService.getPatientById(id);
    }

    @GetMapping
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    @PutMapping
    public Patient updatePatient(@RequestBody Patient patient) {
        return patientService.updatePatient(patient);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return "Patient deleted.";
    }

    @GetMapping("/search/lastname/{lName}")
    public List<Patient> getByLastName(@PathVariable String lName) {
        return patientService.getPatientsByLastName(lName);
    }

    @GetMapping("/search/barangay/{barangay}")
    public List<Patient> getByBarangay(@PathVariable String barangay) {
        return patientService.getPatientsByBarangay(barangay);
    }
}