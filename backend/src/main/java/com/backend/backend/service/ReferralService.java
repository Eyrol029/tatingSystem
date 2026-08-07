package com.backend.backend.service;

import java.util.List;
import java.util.Optional;

import com.backend.backend.model.Referral;

public interface ReferralService {

    List<Referral> getAllReferrals();

    Optional<Referral> getReferralById(Long id);

    List<Referral> getReferralsByPatientId(Long patientId);

    Referral createReferral(Referral referral);

    void sendNotification(Long referralId);

    void deleteReferral(Long id);
}
