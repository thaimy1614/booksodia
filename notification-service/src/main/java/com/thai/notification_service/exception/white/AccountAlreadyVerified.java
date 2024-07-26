package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class AccountAlreadyVerified extends WhiteException {
    private static final String code = "AVV";
    private static final HttpStatus status = HttpStatus.CONFLICT;

    public AccountAlreadyVerified(String email) {
        super(code, email + " is already verified", status);
    }
}
