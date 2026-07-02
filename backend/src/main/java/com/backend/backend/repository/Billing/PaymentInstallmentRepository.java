package com.backend.backend.repository.Billing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Billing.PaymentInstallment;

@Repository
public interface PaymentInstallmentRepository extends JpaRepository<PaymentInstallment, Integer> {
    List<PaymentInstallment> findBySoaId(Integer soaId);
    List<PaymentInstallment> findByPatientServiceId(Integer patientServiceId);
}
