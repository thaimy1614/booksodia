package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class AccountExisted extends WhiteException {
    private static final String code = "AE";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public AccountExisted(String email) {
        super(code, email + " is used", status);
    }
}
