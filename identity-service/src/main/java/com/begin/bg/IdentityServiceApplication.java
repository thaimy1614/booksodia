package com.begin.bg;

import com.begin.bg.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableFeignClients
public class IdentityServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(IdentityServiceApplication.class, args);
	}

	@Bean
	NewTopic sendOtp() {
		return new NewTopic("sendOtp", 3, (short) 3);
	}

	@Bean
	NewTopic sendNewPassword() {
		return new NewTopic("sendNewPassword", 3, (short) 3);
	}

	@Bean
	NewTopic sendVerification() {
		return new NewTopic("verification", 3, (short) 3);
	}

}
