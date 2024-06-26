package com.thai.gateway.repository;

import com.thai.gateway.dto.request.IntrospectRequest;
import com.thai.gateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@Repository
public interface IdentityClient {
    @PostExchange(url = "/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ResponseEntity<IntrospectResponse>> introspect(@RequestBody IntrospectRequest introspectRequest);
}
