package com.thai.notification_service.controller;

import com.thai.notification_service.dto.ResponseObject;
import com.thai.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/connect-sse")
    public SseEmitter subscribe() {
        return notificationService.subscribe();
    }

    @PostMapping("/send")
    String send(){
        notificationService.sendNotification("ABC", "HI HI");
        return "OK";
    }

    @GetMapping
    ResponseObject<>



}
