package com.thai.post_service.repository.httpclient;

import com.thai.post_service.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @GetMapping(value = "/relationship/{user1}/{user2}")
    Boolean createProfile(
            @PathVariable String user1,
            @PathVariable String user2
    );
}
