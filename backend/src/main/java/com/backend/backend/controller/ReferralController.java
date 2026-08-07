package com.backend.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.model.Referral;
import com.backend.backend.service.ReferralService;

@RestController
@RequestMapping("/api/referrals")
@CrossOrigin(origins = "*") // Matches standard Vue dev server
public class ReferralController {

    private final ReferralService referralService;

    @Autowired
    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }
@CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<List<Referral>> getAllReferrals(
            @RequestParam(required = false) Long patientId) {
        if (patientId != null) {
            return ResponseEntity.ok(referralService.getReferralsByPatientId(patientId));
        }
        return ResponseEntity.ok(referralService.getAllReferrals());
    }
@CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<Referral> getReferralById(@PathVariable Long id) {
        return referralService.getReferralById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
@CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity<?> createReferral(@RequestBody Referral referral) {
        try {
            Referral savedReferral = referralService.createReferral(referral);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReferral);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
@CrossOrigin(origins = "*")
    @PostMapping("/{id}/notify")
    public ResponseEntity<String> sendNotification(@PathVariable Long id) {
        try {
            referralService.sendNotification(id);
            return ResponseEntity.ok("Notification sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
@CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReferral(@PathVariable Long id) {
        try {
            referralService.deleteReferral(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
