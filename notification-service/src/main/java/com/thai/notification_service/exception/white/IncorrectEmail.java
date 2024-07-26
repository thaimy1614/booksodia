package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class IncorrectEmail extends WhiteException {
    private static final String code = "IE";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public IncorrectEmail() {
        super(code, "Email is incorrect or does not exist", status);
    }
}
