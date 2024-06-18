package com.thai.emailservice.service;

import com.thai.emailservice.dto.SendOtp;
import com.thai.emailservice.model.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final EmailService emailService;

    @KafkaListener(id="sendOtpGroup", topics = "sendOtp")
    public void listen(SendOtp sendOtp) {
        log.info("Message received: {}", sendOtp.getEmail());
        emailService.sendOtp(sendOtp);
    }
}
