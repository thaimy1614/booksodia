package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class AccountIsBlocked extends WhiteException {
    private static final String code = "AIB";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public AccountIsBlocked(String email) {
        super(code, email + " is blocked by Admin", status);
    }
}
