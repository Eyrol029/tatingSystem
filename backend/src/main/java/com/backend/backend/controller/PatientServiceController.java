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
 @CrossOrigin(origins = "*")
    @PostMapping
    public PatientService addPatientService(@RequestBody PatientService patientService) {
        return patientServiceService.addPatientService(patientService);
    }
 @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public PatientService getPatientService(@PathVariable Integer id) {
        return patientServiceService.getPatientServiceById(id);
    }
 @CrossOrigin(origins = "*")
    @GetMapping
    public List<PatientService> getPatientServices() {
        return patientServiceService.getPatientServices();
    }
 @CrossOrigin(origins = "*")
    @PutMapping
    public PatientService updatePatientService(@RequestBody PatientService patientService) {
        return patientServiceService.updatePatientService(patientService);
    }
 @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public String deletePatientService(@PathVariable Integer id) {
        patientServiceService.deletePatientService(id);
        return "PatientService deleted.";
    }
 @CrossOrigin(origins = "*")
    @GetMapping("/patient/{patientID}")
    public List<PatientService> getByPatientID(@PathVariable Integer patientID) {
        return patientServiceService.getPatientServicesByPatientID(patientID);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/service/{serviceName}")
    public List<PatientService> getByServiceName(@PathVariable String serviceName) {
        return patientServiceService.getPatientServicesByServiceName(serviceName);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/employee/{employeeName}")
    public List<PatientService> getByEmployeeName(@PathVariable String employeeName) {
        return patientServiceService.getPatientServicesByEmployeeName(employeeName);
    }
 @CrossOrigin(origins = "*")
    @GetMapping("/ward/{wardName}")
    public List<PatientService> getByWardName(@PathVariable String wardName) {
        return patientServiceService.getPatientServicesByWardName(wardName);
    }
}
