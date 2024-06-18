package com.thai.emailservice.service;

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

    @KafkaListener(id="notificationGroup", topics = "notification")
    public void listen(MessageDTO messageDTO) {
        log.info("Message received: {}", messageDTO.getTo());
        emailService.sendEmail(messageDTO);
    }
}
