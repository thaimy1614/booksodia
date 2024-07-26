package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class UnverifiedAccount extends WhiteException {
    private static final String code = "UVA";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public UnverifiedAccount(String email) {
        super(code, "Unverified email " + email, status);
    }
}
