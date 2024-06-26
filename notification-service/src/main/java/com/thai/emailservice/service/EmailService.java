package com.thai.emailservice.service;

import com.thai.emailservice.dto.SendOtp;
import com.thai.emailservice.dto.SendPassword;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public interface EmailService {
    void sendOtp(SendOtp sendOtp);

    void sendNewPassword(SendPassword sendPassword);

    void sendHtmlEmail(String name, String to, String url);
}

@Service
@Slf4j
@RequiredArgsConstructor
class EmailServiceImpl implements EmailService {
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    public static final String UTF_8_ENCODING = "UTF-8";
    public static final String EMAIL_TEMPLATE = "email-template";
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendHtmlEmail(String name, String to, String url) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("url", url);
            context.setVariables(Map.of("name", name, "url", url));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(text, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.info("Email sending failed!");
            throw new RuntimeException(e);
        }
    }

    @Override
    @Async
    public void sendOtp(SendOtp sendOtp) {
        try {
            log.info("Sending email...");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(sendOtp.getEmail());
            helper.setFrom(from);
            helper.setText("Your OTP is " + sendOtp.getOtp());
            helper.setSubject(sendOtp.getTopic());

            javaMailSender.send(message);
            log.info("Email sent successful!");
        } catch (MessagingException e) {
            log.info("Email sending failed!");
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendNewPassword(SendPassword sendPassword) {
        try {

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            helper.setTo(sendPassword.getEmail());
            helper.setFrom(from);
            helper.setText("Your new password is " + sendPassword.getPassword());
            helper.setSubject(sendPassword.getTopic());

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
