package com.thai.payment_service.exception.custom;

import fsa.cursus.payment_service.core.exception.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private HttpStatusCode httpStatusCode;
    private ErrorCode errorCode;
    private Map<String, Object> details;
}
