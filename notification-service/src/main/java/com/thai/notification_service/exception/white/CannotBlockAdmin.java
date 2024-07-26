package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class CannotBlockAdmin extends WhiteException {
    private static final String code = "CBA";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public CannotBlockAdmin() {
        super(code, "Cannot block admin", status);
    }
}
