package com.thai.notification_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.notification_service.dto.kafka.NotificationDTO;
import com.thai.notification_service.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class RedisSubscriberService {
    private final NotificationServiceImpl notificationService;
    private final ObjectMapper objectMapper;
    public void handleBellNotification(String message) {
        try {
            NotificationDTO notification = objectMapper.readValue(message, NotificationDTO.class);
            notificationService.sendNotification(notification);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @Bean
    public ChannelTopic bellNotificationTopic() {
        return new ChannelTopic("pushNotification");
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter bellNotificationListenerAdapter,
                                            ChannelTopic bellNotificationTopic
                                            ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(bellNotificationListenerAdapter, bellNotificationTopic);
        return container;
    }

    @Bean
    MessageListenerAdapter bellNotificationListenerAdapter() {
        return new MessageListenerAdapter(this, "handleBellNotification");
    }
}
