package com.thai.emailservice.service;

import com.thai.emailservice.model.MessageDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

public interface EmailService {
    void sendEmail(MessageDTO messageDTO);
}

@Service
@Slf4j
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendEmail(MessageDTO messageDTO) {
        try {
            log.info("Sending email...");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(messageDTO.getTo());
            helper.setFrom(from);
            helper.setText("I LOVE YOU");
            helper.setSubject(messageDTO.getTopic());

            javaMailSender.send(message);
            log.info("Email sent successful!");
        } catch (MessagingException e) {
            log.info("Email sending failed!");
            throw new RuntimeException(e);
        }
    }
}
