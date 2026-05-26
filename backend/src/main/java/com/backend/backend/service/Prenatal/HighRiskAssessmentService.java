package com.backend.backend.service.Prenatal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.backend.model.Prenatal.ClinicalHistoryAndPhysicalExamination;
import com.backend.backend.model.Prenatal.HighRiskAssessmentResult;
import com.backend.backend.model.Prenatal.LaboratoryResults;
import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMedConditions;
import com.backend.backend.model.Prenatal.MedicalSurgicalFactorMeasurements;
import com.backend.backend.model.Prenatal.MedicalSurgicalFactors;
import com.backend.backend.model.Prenatal.ObstetricRiskFactor;
import com.backend.backend.model.Prenatal.PrenatalRecord;
import com.backend.backend.model.Prenatal.VitalSigns;
import com.backend.backend.model.Prenatal.ConsultationRecord;

/**
 * HighRiskAssessmentService
 *
 * Evaluates all prenatal data for a given prenatalRecordID and returns
 * a HighRiskAssessmentResult indicating whether the patient is high-risk
 * and which specific factors triggered the flag.
 */
@Service
public class HighRiskAssessmentService {

    // ── Vital-sign thresholds ──────────────────────────────────────────────
    private static final int    FHT_MIN          = 110;   // bpm
    private static final int    FHT_MAX          = 160;   // bpm
    private static final int    BP_SYSTOLIC_HIGH = 140;   // mmHg
    private static final int    BP_DIASTOLIC_HIGH= 90;    // mmHg
    private static final double HEMOGLOBIN_LOW   = 11.0;  // g/dL (WHO threshold)

    @Autowired private PrenatalRecordService              prenatalRecordService;
    @Autowired private ClinicalHistoryAndPhysicalExaminationService clinicalHistoryService;
    @Autowired private ObstetricRiskFactorService         obstetricRiskFactorService;
    @Autowired private MedicalSurgicalFactorsService      medSurgFactorsService;
    @Autowired private MedicalSurgicalFactorMedConditionsService medConditionsService;
    @Autowired private MedicalSurgicalFactorMeasurementsService  measurementsService;
    @Autowired private LaboratoryResultsService           labResultsService;
    @Autowired private ConsultationRecordService          consultationRecordService;
    @Autowired private VitalSignsService                  vitalSignsService;

    // ══════════════════════════════════════════════════════════════════════
    //  PUBLIC ENTRY POINT
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Assesses high-risk status for a given prenatal record.
     *
     * @param prenatalRecordID the ID of the PrenatalRecord to evaluate
     * @return HighRiskAssessmentResult with flag and reasons list
     */
    public HighRiskAssessmentResult assess(Integer prenatalRecordID) {
        List<String> reasons = new ArrayList<>();

        // 1. PrenatalRecord-level checks (referral, delivery type)
        assessPrenatalRecord(prenatalRecordID, reasons);

        // 2. ClinicalHistory → ObstetricRiskFactors + MedConditions + Measurements
        List<ClinicalHistoryAndPhysicalExamination> clinicalList =
                clinicalHistoryService.getByPrenatalrecordID(prenatalRecordID);

        if (!clinicalList.isEmpty()) {
            Integer clinicalHistoryID = clinicalList.get(0).getClinicalHistoryID();

            // 2a. Obstetric risk factors
            assessObstetricRiskFactors(clinicalHistoryID, reasons);

            // 2b. Medical/surgical conditions
            assessMedicalConditions(clinicalHistoryID, reasons);

            // 2c. Physical measurements (FHT, Leopold)
            assessMeasurements(clinicalHistoryID, reasons);
        }

        // 3. Laboratory results
        assessLaboratoryResults(prenatalRecordID, reasons);

        // 4. Follow-up vital signs (BP, FHT, weight, presentation)
        assessVitalSigns(prenatalRecordID, reasons);

        boolean isHighRisk = !reasons.isEmpty();
        return new HighRiskAssessmentResult(isHighRisk, reasons);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 1 — PrenatalRecord
    // ══════════════════════════════════════════════════════════════════════

    private void assessPrenatalRecord(Integer prenatalRecordID, List<String> reasons) {
        try {
            PrenatalRecord record = prenatalRecordService.getRecordById(prenatalRecordID);
            if (record == null) return;

            if (Boolean.TRUE.equals(record.getReferralHospitalNeeded())) {
                reasons.add("Referral to hospital has been marked as needed");
            }

            String deliveryType = record.getTypeOfDelivery();
            if (deliveryType != null &&
                !deliveryType.trim().isEmpty() &&
                !deliveryType.toLowerCase().contains("normal spontaneous vaginal")) {
                reasons.add("Non-standard delivery type: " + deliveryType);
            }
        } catch (Exception e) {
            // Record may not exist yet; skip gracefully
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 2a — Obstetric Risk Factors
    // ══════════════════════════════════════════════════════════════════════

    private void assessObstetricRiskFactors(Integer clinicalHistoryID, List<String> reasons) {
        List<ObstetricRiskFactor> list =
                obstetricRiskFactorService.getByClinicalHistoryID(clinicalHistoryID);
        if (list.isEmpty()) return;

        ObstetricRiskFactor o = list.get(0);

        if (Boolean.TRUE.equals(o.getMultiplePregnancy()))     reasons.add("Multiple Pregnancy");
        if (Boolean.TRUE.equals(o.getOvarianCyst()))           reasons.add("Ovarian Cyst");
        if (Boolean.TRUE.equals(o.getMyomaUteri()))            reasons.add("Myoma Uteri");
        if (Boolean.TRUE.equals(o.getThyroidDisorder()))       reasons.add("Thyroid Disorder (Obstetric)");
        if (Boolean.TRUE.equals(o.getHistoryOfMiscarriage()))  reasons.add("History of Miscarriage");
        if (Boolean.TRUE.equals(o.getStillbirth()))            reasons.add("History of Stillbirth");
        if (Boolean.TRUE.equals(o.getPreEclampsia()))          reasons.add("Pre-Eclampsia / Eclampsia");
        if (Boolean.TRUE.equals(o.getPrematureContraction()))  reasons.add("Premature Contraction");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 2b — Medical / Surgical Conditions
    // ══════════════════════════════════════════════════════════════════════

    private void assessMedicalConditions(Integer clinicalHistoryID, List<String> reasons) {
        List<MedicalSurgicalFactors> medsurgList =
                medSurgFactorsService.getByClinicalHistoryID(clinicalHistoryID);
        if (medsurgList.isEmpty()) return;

        Integer medsurgID = medsurgList.get(0).getMedsurgID();

        List<MedicalSurgicalFactorMedConditions> condList =
                medConditionsService.getByMedsurgID(medsurgID);
        if (condList.isEmpty()) return;

        MedicalSurgicalFactorMedConditions c = condList.get(0);

        if (Boolean.TRUE.equals(c.getHypertension()))              reasons.add("Hypertension");
        if (Boolean.TRUE.equals(c.getHeartDisease()))              reasons.add("Heart Disease");
        if (Boolean.TRUE.equals(c.getDiabetes()))                  reasons.add("Diabetes");
        if (Boolean.TRUE.equals(c.getThyroidDisorder()))           reasons.add("Thyroid Disorder (Medical)");
        if (Boolean.TRUE.equals(c.getObesity()))                   reasons.add("Obesity");
        if (Boolean.TRUE.equals(c.getModerateToSevereAsthma()))    reasons.add("Moderate to Severe Asthma");
        if (Boolean.TRUE.equals(c.getEpilepsy()))                  reasons.add("Epilepsy");
        if (Boolean.TRUE.equals(c.getRenalDisease()))              reasons.add("Renal Disease");
        if (Boolean.TRUE.equals(c.getBleedingDisorder()))          reasons.add("Bleeding Disorder");
        if (Boolean.TRUE.equals(c.getPreviousCesarianSection()))   reasons.add("Previous Cesarean Section");
        if (Boolean.TRUE.equals(c.getHistoryOfMyomectomy()))       reasons.add("History of Myomectomy");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 2c — Physical Measurements (Leopold / FHT)
    // ══════════════════════════════════════════════════════════════════════

    private void assessMeasurements(Integer clinicalHistoryID, List<String> reasons) {
        List<MedicalSurgicalFactors> medsurgList =
                medSurgFactorsService.getByClinicalHistoryID(clinicalHistoryID);
        if (medsurgList.isEmpty()) return;

        Integer medsurgID = medsurgList.get(0).getMedsurgID();

        List<MedicalSurgicalFactorMeasurements> mList =
                measurementsService.getByMedsurgID(medsurgID);
        if (mList.isEmpty()) return;

        MedicalSurgicalFactorMeasurements m = mList.get(0);

        // Fetal Heart Tone
        if (m.getFetalHeartTone() != null) {
            int fht = m.getFetalHeartTone();
            if (fht < FHT_MIN) {
                reasons.add("Abnormal Fetal Heart Tone (Bradycardia): " + fht + " bpm (< " + FHT_MIN + ")");
            } else if (fht > FHT_MAX) {
                reasons.add("Abnormal Fetal Heart Tone (Tachycardia): " + fht + " bpm (> " + FHT_MAX + ")");
            }
        }

        // Non-vertex presentation flags (L3 reflects presentation)
        checkLeopoldPresentation(m.getLeopoldL3(), "L3", reasons);
        checkLeopoldPresentation(m.getLeopoldL4(), "L4", reasons);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 3 — Laboratory Results
    // ══════════════════════════════════════════════════════════════════════

    private void assessLaboratoryResults(Integer prenatalRecordID, List<String> reasons) {
        List<LaboratoryResults> labList =
                labResultsService.getByPrenatalRecordID(prenatalRecordID);
        if (labList.isEmpty()) return;

        // Use most recent lab result
        LaboratoryResults lab = labList.stream()
                .reduce((a, b) -> a.getLaboratoryResultID() > b.getLaboratoryResultID() ? a : b)
                .orElse(null);
        if (lab == null) return;

        // VDRL positive
        String vdrl = lab.getVenerealDiseaseResearchLaboratoryTest();
        if (isPositive(vdrl)) {
            reasons.add("VDRL Test: Positive result (" + vdrl + ")");
        }

        // HIV positive
        String hiv = lab.getHumanImmunodeficiencyVirusTest();
        if (isPositive(hiv)) {
            reasons.add("HIV Test: Positive result (" + hiv + ")");
        }

        // Hemoglobin below threshold (anemia)
        BigDecimal hgb = lab.getHemoglobin();
        if (hgb != null && hgb.doubleValue() < HEMOGLOBIN_LOW) {
            reasons.add("Low Hemoglobin (Anemia): " + hgb + " g/dL (< " + HEMOGLOBIN_LOW + ")");
        }

        // Hepatitis B positive
        String hbsAg = lab.getHepatitisBSurfaceAntigen();
        if (isPositive(hbsAg)) {
            reasons.add("Hepatitis B Surface Antigen: Positive (" + hbsAg + ")");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SECTION 4 — Follow-Up Visit Vital Signs
    // ══════════════════════════════════════════════════════════════════════

    private void assessVitalSigns(Integer prenatalRecordID, List<String> reasons) {
        List<ConsultationRecord> consultations =
                consultationRecordService.getByPrenatalRecordID(prenatalRecordID);
        if (consultations.isEmpty()) return;

        // Track per-patient flags so we don't repeat the same reason multiple times
        boolean fhtFlagged    = false;
        boolean bpFlagged     = false;
        boolean presFlagged   = false;

        for (ConsultationRecord cr : consultations) {
            List<VitalSigns> vitals =
                    vitalSignsService.getByConsultationRecordID(cr.getConsultationRecordID());

            for (VitalSigns v : vitals) {

                // — Fetal Heart Tone ————————————————————————
                if (!fhtFlagged && v.getFetalHeartTone() != null
                        && !v.getFetalHeartTone().isBlank()) {
                    try {
                        int fht = Integer.parseInt(v.getFetalHeartTone().trim());
                        if (fht < FHT_MIN) {
                            reasons.add("Follow-up Visit: Fetal Heart Tone Bradycardia (" + fht + " bpm)");
                            fhtFlagged = true;
                        } else if (fht > FHT_MAX) {
                            reasons.add("Follow-up Visit: Fetal Heart Tone Tachycardia (" + fht + " bpm)");
                            fhtFlagged = true;
                        }
                    } catch (NumberFormatException ignored) {}
                }

                // — Blood Pressure ——————————————————————————
                if (!bpFlagged && v.getBloodPressure() != null
                        && !v.getBloodPressure().isBlank()) {
                    if (isHighBloodPressure(v.getBloodPressure())) {
                        reasons.add("Follow-up Visit: High Blood Pressure detected (" + v.getBloodPressure() + ")");
                        bpFlagged = true;
                    }
                }

                // — Fetal Presentation ——————————————————————
                if (!presFlagged && v.getFetalPresentation() != null
                        && !v.getFetalPresentation().isBlank()) {
                    String pres = v.getFetalPresentation().toLowerCase();
                    if (pres.contains("breech") || pres.contains("transverse")
                            || pres.contains("oblique") || pres.contains("face")
                            || pres.contains("brow") || pres.contains("shoulder")) {
                        reasons.add("Follow-up Visit: Abnormal fetal presentation (" + v.getFetalPresentation() + ")");
                        presFlagged = true;
                    }
                }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Returns true if a lab result text indicates a positive result.
     * Handles: "positive", "pos", "reactive", "+", "dirty", "abnormal"
     */
    private boolean isPositive(String value) {
        if (value == null || value.isBlank()) return false;
        String v = value.toLowerCase().trim();
        return v.contains("positive") || v.contains("pos")
                || v.contains("reactive") || v.equals("+")
                || v.contains("dirty")   || v.contains("abnormal");
    }

    /**
     * Parses a "120/80" style blood pressure string and checks against thresholds.
     */
    private boolean isHighBloodPressure(String bp) {
        if (bp == null || bp.isBlank()) return false;
        try {
            String[] parts = bp.trim().split("[/\\-]");
            if (parts.length < 2) return false;
            int systolic  = Integer.parseInt(parts[0].trim());
            int diastolic = Integer.parseInt(parts[1].trim());
            return systolic >= BP_SYSTOLIC_HIGH || diastolic >= BP_DIASTOLIC_HIGH;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks Leopold maneuver text for abnormal (non-vertex) presentation keywords.
     */
    private void checkLeopoldPresentation(String leopold, String label, List<String> reasons) {
        if (leopold == null || leopold.isBlank()) return;
        String v = leopold.toLowerCase();
        if (v.contains("breech") || v.contains("transverse")
                || v.contains("oblique") || v.contains("face")
                || v.contains("brow")    || v.contains("footling")) {
            reasons.add("Leopold " + label + ": Abnormal fetal presentation (" + leopold + ")");
        }
    }
}