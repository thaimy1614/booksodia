package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class BadToken extends WhiteException {
    private static final String code = "BT";
    private static final String msg = "JWT token is invalid";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public BadToken() {
        super(code, msg, status);
    }

    public BadToken(Throwable e) {
        super(code, msg, status, e);
    }
}
