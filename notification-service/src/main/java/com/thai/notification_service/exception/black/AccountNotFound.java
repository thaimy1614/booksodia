package com.thai.notification_service.exception.black;

import org.springframework.http.HttpStatus;

public class AccountNotFound extends BlackException {
    private static final String code = "ENF";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public AccountNotFound(Long userId) {
        super(code, "No account with id " + userId, status);
    }
}
