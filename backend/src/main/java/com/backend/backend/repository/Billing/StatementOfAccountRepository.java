package com.backend.backend.repository.Billing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.backend.model.Billing.StatementOfAccount;

@Repository
public interface StatementOfAccountRepository extends JpaRepository<StatementOfAccount, Integer> {
    List<StatementOfAccount> findByPatientID(Integer patientID);
    void deleteByPatientID(Integer patientID);
}
