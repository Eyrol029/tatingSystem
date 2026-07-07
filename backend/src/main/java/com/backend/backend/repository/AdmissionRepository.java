package com.backend.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backend.model.Admission;

public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
}