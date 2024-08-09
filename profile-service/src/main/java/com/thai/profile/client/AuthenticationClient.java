package com.thai.profile.client;

import com.thai.profile.configuration.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "authenticationService",
        url = "http://identity-service:8080/identity",
        configuration = {AuthenticationRequestInterceptor.class}
)
public interface AuthenticationClient {
    @PostMapping(value = "/approve-instructor")
    Boolean approveInstructor(@RequestParam("userId") Long userId);
}
