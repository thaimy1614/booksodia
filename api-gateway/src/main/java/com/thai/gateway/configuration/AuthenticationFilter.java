package com.thai.gateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thai.gateway.dto.ResponseObject;
import com.thai.gateway.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final IdentityService identityService;
    private final ObjectMapper mapper;
    String[] PUBLIC_ENDPOINTS = {
            "/identity/auth",
            "/identity/log-out",
            "/identity/introspect",
            "/identity/signup",
            "/identity/change-password",
            "/identity/forget-password/send-otp",
            "/identity/forget-password/check-otp",
            "/identity/verify",
            "/identity/outbound/authentication"
    };
    @Value("${app.api-prefix}")
    private String prefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("{}", isPublicEndpoint(exchange.getRequest()));
        log.info(exchange.getRequest().getPath().toString());
        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }
        // Get token from authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                return unauthenticated(exchange.getResponse());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        String token = authHeader.replace("Bearer ", "");
        log.info(token);
        // Verify token - call identity service
        return identityService.introspect(token).flatMap(responseObjectResponseEntity -> {
            log.info(String.valueOf(responseObjectResponseEntity.getBody().isValid()));
            if (responseObjectResponseEntity.getBody().isValid()) {
                return chain.filter(exchange);  // Chain the introspect call with the filter chain
            } else {
                try {
                    return unauthenticated(exchange.getResponse());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }).onErrorResume(throwable -> {
            try {
                return unauthenticated(exchange.getResponse());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(PUBLIC_ENDPOINTS).anyMatch(s ->
        {
            log.info(s);
            return request.getURI().getPath().matches(prefix + s);
        });
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) throws JsonProcessingException {
        ResponseObject responseObject = ResponseObject
                .builder()
                .status("FAIL")
                .message("UNAUTHENTICATED")
                .build();
        String body = mapper.writeValueAsString(responseObject);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
