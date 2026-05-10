package com.backend.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.backend.backend.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Integer> {

    List<Patient> findAll();

    List<Patient> findByLName(String lName);

    List<Patient> findByBarangay(String barangay);

    Optional<Patient> findByEmail(String email);
}
