package com.begin.bg.repositories.httpclient;

import com.begin.bg.configuration.AuthenticationRequestInterceptor;
import com.begin.bg.dto.request.ProfileCreationRequest;
import com.begin.bg.dto.response.ProfileCreationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = {AuthenticationRequestInterceptor.class})
public interface ProfileClient {
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    Boolean createProfile(@RequestBody ProfileCreationRequest request);
}
