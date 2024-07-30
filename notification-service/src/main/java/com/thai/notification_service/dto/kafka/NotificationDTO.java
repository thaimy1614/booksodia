package com.thai.notification_service.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String message;
    private String image;
    private String topic;
    private String[] to;
    private String from;
}
