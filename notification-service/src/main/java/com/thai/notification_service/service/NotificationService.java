package com.thai.notification_service.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void sendNotification(String userId, String notification);
    SseEmitter subscribe(String userId);
}
