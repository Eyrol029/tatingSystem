package com.backend.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.backend.model.Referral;
import com.backend.backend.repository.ReferralRepository;

@Service
@Transactional
public class ReferralServiceImpl implements ReferralService {

    private final ReferralRepository referralRepository;

    @Autowired
    public ReferralServiceImpl(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Referral> getAllReferrals() {
        return referralRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Referral> getReferralById(Long id) {
        return referralRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Referral> getReferralsByPatientId(Long patientId) {
        return referralRepository.findByPatientId(patientId);
    }

    @Override
    public Referral createReferral(Referral referral) {
        if (referral.getPatientName() == null || referral.getPatientName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be null or empty.");
        }
        return referralRepository.save(referral);
    }

    @Override
    public void sendNotification(Long referralId) {
        Referral referral = referralRepository.findById(referralId)
                .orElseThrow(() -> new IllegalArgumentException("Referral not found with ID: " + referralId));

        // Logic for SMS/Email/Push notification to OB-GYN contact
        System.out.println("Notification sent to OB-GYN (" + referral.getObGyneName() + ") at " + referral.getObGyneContact());
    }

    @Override
    public void deleteReferral(Long id) {
        if (!referralRepository.existsById(id)) {
            throw new IllegalArgumentException("Referral not found with ID: " + id);
        }
        referralRepository.deleteById(id);
    }
}
