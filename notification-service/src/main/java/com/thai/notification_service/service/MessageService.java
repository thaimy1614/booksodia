package com.thai.notification_service.service;

import com.thai.notification_service.dto.kafka.NotificationDTO;
import com.thai.notification_service.dto.kafka.SendOtp;
import com.thai.notification_service.dto.kafka.SendPassword;
import com.thai.notification_service.dto.kafka.VerifyAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final EmailService emailService;

    @KafkaListener(id = "sendOtpGroup", topics = "sendOtp")
    public void listenSendOtp(SendOtp sendOtp) {
        emailService.sendOtp(sendOtp);
    }

    @KafkaListener(id = "sendPasswordGroup", topics = "sendNewPassword")
    public void listenSendPassword(SendPassword sendPassword) {
        emailService.sendNewPassword(sendPassword);
    }

    @KafkaListener(id = "verificationGroup", topics = "verification")
    public void listenVerification(VerifyAccount verifyAccount) {
        emailService.sendHtmlEmail(verifyAccount.getFullName(), verifyAccount.getEmail(), verifyAccount.getUrl());
    }
}
