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
    // TODO: Replace Hashmap with Redis for scale different instance
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();
    private static final String HEARTBEAT_MESSAGE = "heartbeat";
    private static final long HEARTBEAT_INTERVAL = 30; // 30 seconds
    private static final long TIMEOUT_DURATION = 60_000L; // 60 seconds

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void sendNotification(String userId, String notification) {
        CopyOnWriteArrayList<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    emitter.send(notification);
                } catch (IOException e) {
                    log.info(e.getMessage());
                    userEmitters.remove(emitter);
                }
            }
        }
    }

    public SseEmitter subscribe() {
        // Get userId via Spring security context holder
        String userId = "ABC";
        CopyOnWriteArrayList<SseEmitter> userEmitters = emitters.get(userId);
        // maximum 5 connects each user
        if (userEmitters.size()>=5) {
            userEmitters.clear();
        }
        SseEmitter emitter = new SseEmitter(TIMEOUT_DURATION); // No timeout
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(userId, emitter));
        emitter.onTimeout(() -> {
            removeEmitter(userId, emitter);
        });
        emitter.onError((ex) -> removeEmitter(userId, emitter));
        startHeartbeat(userId, emitter);
        return emitter;
    }


    private void removeEmitter(String userId, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            log.info("Removed emitter for userId: {}", userId);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
                log.info("All emitters removed for userId: {}", userId);
            }
        }
    }

    private void startHeartbeat(String userId, SseEmitter emitter) {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().data(HEARTBEAT_MESSAGE));
            } catch (IOException e) {
                // Handle error: possibly remove emitter, log information, etc.
                removeEmitter(userId, emitter);
            }
        }, 0, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

}


