package com.thai.profile.exception.white;

import org.springframework.http.HttpStatus;

public class CustomValidationException extends WhiteException {
    private static final String code = "CV";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public CustomValidationException(String message) {
        super(code, message, status);
    }
}
