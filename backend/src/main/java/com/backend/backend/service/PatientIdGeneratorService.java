package com.backend.backend.service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.ClinicalService;
import com.backend.backend.model.Patient;
import com.backend.backend.model.PatientService;
import com.backend.backend.repository.ClinicalServiceRepository;
import com.backend.backend.repository.PatientRepository;
import com.backend.backend.repository.PatientServiceRepository;

@Service
public class PatientIdGeneratorService {

    @Autowired
    private ClinicalServiceRepository clinicalServiceRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceRepository patientServiceRepository;

    private static final Pattern BASE_PATTERN = Pattern.compile("^(.*?)(\\d+)$");

    /**
     * Generates the next sequential ID/case number for the given clinical service name.
     * Example: If base case number is "26-00-000", first patient gets "26-00-001", next "26-00-002", etc.
     */
    public synchronized String generateNextPatientId(String serviceName) {
        String baseCaseNumber = resolveBaseCaseNumber(serviceName);

        String prefix;
        int paddingLength;

        Matcher matcher = BASE_PATTERN.matcher(baseCaseNumber);
        if (matcher.matches()) {
            prefix = matcher.group(1);
            paddingLength = Math.max(3, matcher.group(2).length());
        } else {
            prefix = baseCaseNumber.endsWith("-") ? baseCaseNumber : baseCaseNumber + "-";
            paddingLength = 3;
        }

        // Find maximum existing sequence number for this prefix
        int maxSeq = 0;

        List<Patient> patients = patientRepository.findByPatientCodeStartingWith(prefix);
        if (patients != null) {
            for (Patient p : patients) {
                int seq = extractSequence(p.getPatientCode(), prefix);
                if (seq > maxSeq) {
                    maxSeq = seq;
                }
            }
        }

        List<PatientService> services = patientServiceRepository.findByCaseNumberStartingWith(prefix);
        if (services != null) {
            for (PatientService ps : services) {
                int seq = extractSequence(ps.getCaseNumber(), prefix);
                if (seq > maxSeq) {
                    maxSeq = seq;
                }
            }
        }

        int nextSeq = maxSeq + 1;
        return prefix + String.format("%0" + paddingLength + "d", nextSeq);
    }

    private int extractSequence(String code, String prefix) {
        if (code == null || !code.startsWith(prefix)) {
            return 0;
        }
        try {
            String suffix = code.substring(prefix.length()).trim();
            // In case suffix has trailing non-digits, take leading digits
            Matcher m = Pattern.compile("^(\\d+)").matcher(suffix);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    private String resolveBaseCaseNumber(String serviceName) {
        String currentYear2Digits = String.valueOf(Year.now().getValue()).substring(2);

        if (serviceName != null && !serviceName.trim().isEmpty()) {
            String trimmed = serviceName.trim();
            Optional<ClinicalService> csOpt = clinicalServiceRepository.findByNameIgnoreCase(trimmed);
            if (csOpt.isPresent()) {
                ClinicalService cs = csOpt.get();
                if (cs.getCaseNumber() != null && !cs.getCaseNumber().trim().isEmpty() && !"---".equals(cs.getCaseNumber().trim())) {
                    return cs.getCaseNumber().trim();
                }
            }

            // Check if any catalog service name contains this
            List<ClinicalService> allCatalog = clinicalServiceRepository.findAll();
            for (ClinicalService cs : allCatalog) {
                if (cs.getName() != null && cs.getName().equalsIgnoreCase(trimmed)) {
                    if (cs.getCaseNumber() != null && !cs.getCaseNumber().trim().isEmpty() && !"---".equals(cs.getCaseNumber().trim())) {
                        return cs.getCaseNumber().trim();
                    }
                }
            }

            // Fallback base format based on common service names
            String lower = trimmed.toLowerCase();
            if (lower.contains("prenatal")) {
                return currentYear2Digits + "-00-000";
            } else if (lower.contains("family") || lower.contains("fp")) {
                return currentYear2Digits + "-01-000";
            } else if (lower.contains("ultrasound")) {
                return currentYear2Digits + "-02-000";
            } else if (lower.contains("admission") || lower.contains("lying") || lower.contains("maternity")) {
                return currentYear2Digits + "-03-000";
            } else if (lower.contains("labor") || lower.contains("lab")) {
                return currentYear2Digits + "-04-000";
            } else if (lower.contains("postpartum")) {
                return currentYear2Digits + "-05-000";
            }
        }

        return currentYear2Digits + "-00-000";
    }
}
