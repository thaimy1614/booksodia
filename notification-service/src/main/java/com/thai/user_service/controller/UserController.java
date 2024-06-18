package com.thai.user_service.controller;

import com.thai.user_service.dto.MessageDTO;
import com.thai.user_service.entity.User;
import com.thai.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.kafka.core.KafkaTemplate;
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User user) {
        User u = userService.createUser(user);
        MessageDTO messageDTO = MessageDTO
                .builder()
                .message("Hello "+ u.getEmail().substring(0,u.getEmail().length()-10))
                .topic("Test Kafka")
                .from("")
                .to(u.getEmail())
                .build();
        kafkaTemplate.send("notification", messageDTO);
        return ResponseEntity.ok(u);
    }
}
