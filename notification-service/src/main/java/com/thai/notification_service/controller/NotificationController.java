package com.thai.notification_service.controller;

import com.thai.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/connect-sse")
    SseEmitter subscribe(@RequestParam("userId") String userId) {
        return notificationService.subscribe(userId);
    }

    @PostMapping("/send")
    String send() {
        notificationService.sendNotification("123", "HI HI");
        return "OK";
    }

}
