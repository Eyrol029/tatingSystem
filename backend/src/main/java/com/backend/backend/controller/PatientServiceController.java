package com.backend.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.PatientService;
import com.backend.backend.service.PatientServiceService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/patient-services")
public class PatientServiceController {

    @Autowired
    private PatientServiceService patientServiceService;

    @PostMapping
    public PatientService addPatientService(@RequestBody PatientService patientService) {
        return patientServiceService.addPatientService(patientService);
    }

    @GetMapping("/{id}")
    public PatientService getPatientService(@PathVariable Integer id) {
        return patientServiceService.getPatientServiceById(id);
    }

    @GetMapping
    public List<PatientService> getPatientServices() {
        return patientServiceService.getPatientServices();
    }

    @PutMapping
    public PatientService updatePatientService(@RequestBody PatientService patientService) {
        return patientServiceService.updatePatientService(patientService);
    }

    @DeleteMapping("/{id}")
    public String deletePatientService(@PathVariable Integer id) {
        patientServiceService.deletePatientService(id);
        return "PatientService deleted.";
    }

    @GetMapping("/patient/{patientID}")
    public List<PatientService> getByPatientID(@PathVariable Integer patientID) {
        return patientServiceService.getPatientServicesByPatientID(patientID);
    }

    @GetMapping("/service/{serviceID}")
    public List<PatientService> getByServiceID(@PathVariable Integer serviceID) {
        return patientServiceService.getPatientServicesByServiceID(serviceID);
    }

    @GetMapping("/employee/{employeeID}")
    public List<PatientService> getByEmployeeID(@PathVariable Integer employeeID) {
        return patientServiceService.getPatientServicesByEmployeeID(employeeID);
    }

    @GetMapping("/ward/{wardID}")
    public List<PatientService> getByWardID(@PathVariable Integer wardID) {
        return patientServiceService.getPatientServicesByWardID(wardID);
    }
}
