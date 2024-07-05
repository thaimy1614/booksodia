package com.thai.payment_service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@SpringBootApplication
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Bean
    NewTopic paymentTopic() {
        return new NewTopic("payment-status", 1, (short) 3);
    }

    @Bean
    JsonMessageConverter converter() {
        return new JsonMessageConverter();
    }

}
