package com.thai.order_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    JsonMessageConverter converter() {
        return new JsonMessageConverter();
    }

    @Bean
    NewTopic createOrderTopic() {
        return new NewTopic("created-order", 1, (short) 3);
    }
}
