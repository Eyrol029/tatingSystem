package com.backend.backend.model.Prenatal;
 
import java.util.List;
 
/**
 * DTO returned by the high-risk assessment endpoint.
 * Contains the final flag and every individual reason that triggered it.
 */
public class HighRiskAssessmentResult {
 
    private boolean highRisk;
    private List<String> reasons;
 
    public HighRiskAssessmentResult(boolean highRisk, List<String> reasons) {
        this.highRisk = highRisk;
        this.reasons = reasons;
    }
 
    public boolean isHighRisk() { return highRisk; }
    public void setHighRisk(boolean highRisk) { this.highRisk = highRisk; }
 
    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
 