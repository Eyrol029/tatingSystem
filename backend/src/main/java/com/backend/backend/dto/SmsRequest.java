package com.backend.backend.dto;

public class SmsRequest {
    private String recipient;
    private String message;
    private Long patientId;
    private Integer soaId;

    public SmsRequest() {
    }

    public SmsRequest(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Integer getSoaId() {
        return soaId;
    }

    public void setSoaId(Integer soaId) {
        this.soaId = soaId;
    }
}
