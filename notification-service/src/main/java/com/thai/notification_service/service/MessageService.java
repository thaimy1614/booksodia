package com.thai.notification_service.service;

import com.thai.notification_service.dto.SendOtp;
import com.thai.notification_service.dto.SendPassword;
import com.thai.notification_service.dto.VerifyAccount;
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
        log.info("Message received: {}", sendOtp.getEmail());
        emailService.sendOtp(sendOtp);
    }

    @KafkaListener(id = "sendPasswordGroup", topics = "sendNewPassword")
    public void listenSendPassword(SendPassword sendPassword) {
        log.info("Message received: {}", sendPassword.getEmail());
        emailService.sendNewPassword(sendPassword);
    }

    @KafkaListener(id = "verificationGroup", topics = "verification")
    public void listenVerification(VerifyAccount verifyAccount) {
        emailService.sendHtmlEmail(verifyAccount.getFullName(), verifyAccount.getEmail(), verifyAccount.getUrl());
    }
}
