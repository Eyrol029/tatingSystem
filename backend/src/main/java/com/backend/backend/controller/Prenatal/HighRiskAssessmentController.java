package com.backend.backend.controller.Prenatal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.backend.model.Prenatal.HighRiskAssessmentResult;
import com.backend.backend.service.Prenatal.HighRiskAssessmentService;

/**
 * Exposes a single GET endpoint:
 *   GET /api/prenatal/high-risk-assessment/{prenatalRecordID}
 *
 * Response example:
 * {
 *   "highRisk": true,
 *   "reasons": [
 *     "Hypertension",
 *     "Follow-up Visit: High Blood Pressure detected (150/95)",
 *     "Low Hemoglobin (Anemia): 9.5 g/dL (< 11.0)"
 *   ]
 * }
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/prenatal/high-risk-assessment")
public class HighRiskAssessmentController {

    @Autowired
    private HighRiskAssessmentService highRiskAssessmentService;

    /**
     * Assesses high-risk status for a prenatal record.
     *
     * @param prenatalRecordID ID of the PrenatalRecord
     * @return 200 OK with HighRiskAssessmentResult JSON
     */
    @CrossOrigin(origins = "*")
    @GetMapping("/{prenatalRecordID}")
    public ResponseEntity<HighRiskAssessmentResult> assess(
            @PathVariable Integer prenatalRecordID) {
        HighRiskAssessmentResult result = highRiskAssessmentService.assess(prenatalRecordID);
        return ResponseEntity.ok(result);
    }
}