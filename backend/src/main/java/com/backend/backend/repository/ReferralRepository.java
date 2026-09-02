package com.backend.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    List<Referral> findByPatientId(Long patientId);

    void deleteByPatientId(Long patientId);

    List<Referral> findByPatientNameContainingIgnoreCase(String patientName);
}
