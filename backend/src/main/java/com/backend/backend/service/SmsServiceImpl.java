package com.backend.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.backend.backend.dto.SmsRequest;
import com.backend.backend.dto.SmsResponse;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${sms.mock-mode:false}")
    private boolean mockMode;

    @Value("${sms.semaphore.api-key:}")
    private String semaphoreApiKey;

    @Value("${sms.semaphore.sender-name:}")
    private String semaphoreSenderName;

    @Value("${sms.semaphore.api-url:https://api.semaphore.co/api/v4/messages}")
    private String semaphoreApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        if (request == null || request.getRecipient() == null || request.getRecipient().isBlank()) {
            return new SmsResponse(false, "Recipient phone number is required.", null, "Validation Error");
        }

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return new SmsResponse(false, "SMS message cannot be empty.", null, "Validation Error");
        }

        String formattedNumber = formatPhilippineNumber(request.getRecipient());

        // Check if running in mock mode
        if (mockMode) {
            System.out.println("==================================================");
            System.out.println("[SMS GATEWAY - MOCK MODE]");
            System.out.println("To: " + formattedNumber);
            System.out.println("Message: \n" + request.getMessage());
            System.out.println("==================================================");

            return new SmsResponse(
                    true,
                    "SMS sent successfully (Mock Mode / Simulated)",
                    "mock-msg-" + System.currentTimeMillis(),
                    "Sent"
            );
        }

        // Validate Semaphore API Key
        if (semaphoreApiKey == null || semaphoreApiKey.isBlank()
                || "YOUR_SEMAPHORE_API_KEY_HERE".equalsIgnoreCase(semaphoreApiKey.trim())) {
            return new SmsResponse(
                    false,
                    "Semaphore API Key is missing. Please check your application.properties.",
                    null,
                    "Config Error"
            );
        }

        // Live Semaphore Dispatch
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("apikey", semaphoreApiKey.trim());
            map.add("number", formattedNumber);
            map.add("message", request.getMessage());

            if (semaphoreSenderName != null && !semaphoreSenderName.isBlank()
                    && !"SEMAPHORE".equalsIgnoreCase(semaphoreSenderName.trim())) {
                map.add("sendername", semaphoreSenderName.trim());
            }

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(semaphoreApiUrl, entity, String.class);

            System.out.println("Semaphore Response: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return new SmsResponse(
                        true,
                        "SMS successfully dispatched to " + formattedNumber,
                        null,
                        "Delivered"
                );
            } else {
                return new SmsResponse(
                        false,
                        "Gateway returned status: " + response.getStatusCode(),
                        null,
                        "Failed"
                );
            }
        } catch (HttpStatusCodeException e) {
            String errorBody = e.getResponseBodyAsString();
            System.err.println("SMS Gateway HTTP Error (" + e.getStatusCode() + "): " + errorBody);
            return new SmsResponse(
                    false,
                    "SMS Gateway Error (" + e.getStatusCode() + "): " + (errorBody.isEmpty() ? e.getMessage() : errorBody),
                    null,
                    "Failed"
            );
        } catch (Exception e) {
            System.err.println("SMS Gateway Error: " + e.getMessage());
            return new SmsResponse(
                    false,
                    "Failed to send SMS: " + e.getMessage(),
                    null,
                    "Failed"
            );
        }
    }

    private String formatPhilippineNumber(String raw) {
        if (raw == null) return "";
        String cleaned = raw.replaceAll("[^0-9]", "");
        if (cleaned.startsWith("09") && cleaned.length() == 11) {
            return cleaned;
        }
        if (cleaned.startsWith("639") && cleaned.length() == 12) {
            return "0" + cleaned.substring(2);
        }
        if (cleaned.startsWith("9") && cleaned.length() == 10) {
            return "0" + cleaned;
        }
        return cleaned;
    }
}
