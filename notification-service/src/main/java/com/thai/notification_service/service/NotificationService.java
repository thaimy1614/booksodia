package com.thai.notification_service.service;

import com.thai.notification_service.dto.kafka.NotificationDTO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    void sendNotification(NotificationDTO notification);

    SseEmitter subscribe(String userId);
}
