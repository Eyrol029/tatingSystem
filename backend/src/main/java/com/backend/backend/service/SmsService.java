package com.backend.backend.service;

import com.backend.backend.dto.SmsRequest;
import com.backend.backend.dto.SmsResponse;

public interface SmsService {
    SmsResponse sendSms(SmsRequest request);
}
