package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class PasswordMismatch extends WhiteException {
    private static final String code = "PM";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public PasswordMismatch() {
        super(code, "Old password is incorrect", status);
    }
}
