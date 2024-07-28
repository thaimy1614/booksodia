package com.thai.notification_service.service.impl;

import com.thai.notification_service.service.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private static final String HEARTBEAT_MESSAGE = "heartbeat";
    private static final long HEARTBEAT_INTERVAL = 30; // 30 seconds
    private static final long TIMEOUT_DURATION = 60_000L; // 60 seconds
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public NotificationServiceImpl() {
        startHeartbeat();
    }

    public void sendNotification(String userId, String notification) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.removeIf(emitter -> {
                try {
                    emitter.send(notification);
                    return false;
                } catch (IOException e) {
                    return true; // Remove emitter if an error occurs
                }
            });
        }
        log.info("emitters: {}", emitters.toString());
    }

    public SseEmitter subscribe(String userId) {
        List<SseEmitter> userEmitters = emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>());
        if (!userEmitters.isEmpty()) {
            userEmitters.clear();
        }

        SseEmitter emitter = new SseEmitter(TIMEOUT_DURATION);
        userEmitters.add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> removeEmitter(userId, emitter));
        emitter.onError((ex) -> removeEmitter(userId, emitter));

        log.info(emitters.toString());
        return emitter;
    }

    private void removeEmitter(String userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
        log.info("Removed emitter for userId: {}", userId);
    }

    private void startHeartbeat() {
        scheduler.scheduleAtFixedRate(() -> {
            emitters.forEach((userId, userEmitters) -> {
                userEmitters.removeIf(emitter -> {
                    try {
                        emitter.send(SseEmitter.event().data(HEARTBEAT_MESSAGE));
                        return false;
                    } catch (IOException e) {
                        return true; // Remove emitter if an error occurs
                    }
                });
            });
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void onDestroy() {
        log.info("Shutting down NotificationService...");
        shutdown();
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
