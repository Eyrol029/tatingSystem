package com.backend.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.backend.dto.SmsRequest;
import com.backend.backend.dto.SmsResponse;
import com.backend.backend.service.SmsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public SmsResponse sendSms(@RequestBody SmsRequest request) {
        return smsService.sendSms(request);
    }
}
