package com.backend.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.backend.model.ClinicalService;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.repository.ClinicalServiceRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;

@ExtendWith(MockitoExtension.class)
public class PatientIdGeneratorServiceTest {

    @Mock
    private ClinicalServiceRepository clinicalServiceRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientServiceRepository patientServiceRepository;

    @InjectMocks
    private PatientIdGeneratorService patientIdGeneratorService;

    private ClinicalService prenatalService;
    private ClinicalService fpService;

    @BeforeEach
    void setUp() {
        prenatalService = new ClinicalService();
        prenatalService.setId(1L);
        prenatalService.setName("Prenatal");
        prenatalService.setCaseNumber("26-00-000");

        fpService = new ClinicalService();
        fpService.setId(2L);
        fpService.setName("Family Planning");
        fpService.setCaseNumber("26-01-000");
    }

    @Test
    void testFirstPrenatalPatientIdGeneratedAs26_00_001() {
        when(clinicalServiceRepository.findByNameIgnoreCase("Prenatal")).thenReturn(Optional.of(prenatalService));
        when(patientRepository.findByPatientCodeStartingWith("26-00-")).thenReturn(new ArrayList<>());
        when(patientServiceRepository.findByCaseNumberStartingWith("26-00-")).thenReturn(new ArrayList<>());

        String generatedId = patientIdGeneratorService.generateNextPatientId("Prenatal");
        assertEquals("26-00-001", generatedId);
    }

    @Test
    void testSequentialIncrementForExistingPatients() {
        when(clinicalServiceRepository.findByNameIgnoreCase("Prenatal")).thenReturn(Optional.of(prenatalService));

        Patient p1 = new Patient();
        p1.setPatientCode("26-00-001");
        Patient p2 = new Patient();
        p2.setPatientCode("26-00-002");

        when(patientRepository.findByPatientCodeStartingWith("26-00-")).thenReturn(List.of(p1, p2));
        when(patientServiceRepository.findByCaseNumberStartingWith("26-00-")).thenReturn(new ArrayList<>());

        String generatedId = patientIdGeneratorService.generateNextPatientId("Prenatal");
        assertEquals("26-00-003", generatedId);
    }

    @Test
    void testFamilyPlanningServiceHasIndependentSequence() {
        when(clinicalServiceRepository.findByNameIgnoreCase("Family Planning")).thenReturn(Optional.of(fpService));
        when(patientRepository.findByPatientCodeStartingWith("26-01-")).thenReturn(new ArrayList<>());
        when(patientServiceRepository.findByCaseNumberStartingWith("26-01-")).thenReturn(new ArrayList<>());

        String generatedId = patientIdGeneratorService.generateNextPatientId("Family Planning");
        assertEquals("26-01-001", generatedId);
    }
}
