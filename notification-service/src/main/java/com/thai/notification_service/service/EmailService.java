package com.thai.notification_service.service;

import com.thai.notification_service.dto.kafka.SendOtp;
import com.thai.notification_service.dto.kafka.SendPassword;

public interface EmailService {
    void sendOtp(SendOtp sendOtp);

    void sendNewPassword(SendPassword sendPassword);

    void sendHtmlEmail(String name, String to, String url);
}

