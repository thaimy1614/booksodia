package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class IncorrectPassword extends WhiteException {
    private static final String code = "IP";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public IncorrectPassword() {
        super(code, "Password is incorrect", status);
    }
}
