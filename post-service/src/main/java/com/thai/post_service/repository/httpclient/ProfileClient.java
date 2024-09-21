package com.thai.post_service.repository.httpclient;

import com.thai.post_service.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "/relationship/{user1}/{user2}", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean createProfile(@PathVariable String user1, @PathVariable String user2);
}
