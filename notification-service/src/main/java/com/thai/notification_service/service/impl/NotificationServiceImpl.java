package com.thai.notification_service.service.impl;

import com.thai.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.*;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private static final String HEARTBEAT_MESSAGE = "heartbeat";
    private static final long HEARTBEAT_INTERVAL = 30; // 30 seconds
    private static final long TIMEOUT_DURATION = 60_000L; // 60 seconds

    private final ConcurrentHashMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ScheduledFuture<?>> heartbeatSchedulers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void sendNotification(String userId, String notification) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(notification);
            } catch (IOException e) {
                log.info(e.getMessage());
                removeEmitter(userId);
            }
        }
    }

    public SseEmitter subscribe(String userId) {
        // Remove previous emitter and scheduler if exist
        removeEmitter(userId);

        SseEmitter emitter = new SseEmitter(TIMEOUT_DURATION);
        emitters.put(userId, emitter);
        ScheduledFuture<?> heartbeatTask = startHeartbeat(userId, emitter);
        heartbeatSchedulers.put(userId, heartbeatTask);

        emitter.onCompletion(() -> removeEmitter(userId));
        emitter.onTimeout(() -> removeEmitter(userId));
        emitter.onError((ex) -> removeEmitter(userId));

        log.info(emitters.toString());
        return emitter;
    }

    private void removeEmitter(String userId) {
        SseEmitter emitter = emitters.remove(userId);
        if (emitter != null) {
            emitter.complete();
            log.info("Removed emitter for userId: {}", userId);
        }

        ScheduledFuture<?> task = heartbeatSchedulers.remove(userId);
        if (task != null) {
            task.cancel(true);
            log.info("Stopped heartbeat for userId: {}", userId);
        }
    }

    private ScheduledFuture<?> startHeartbeat(String userId, SseEmitter emitter) {
        return scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().data(HEARTBEAT_MESSAGE));
            } catch (IOException e) {
                log.info("Error sending heartbeat, removing emitter for userId: {}", userId);
                removeEmitter(userId);
            }
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
