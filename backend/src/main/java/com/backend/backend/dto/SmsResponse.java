package com.backend.backend.dto;

public class SmsResponse {
    private boolean success;
    private String message;
    private String messageId;
    private String status;

    public SmsResponse() {
    }

    public SmsResponse(boolean success, String message, String messageId, String status) {
        this.success = success;
        this.message = message;
        this.messageId = messageId;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
