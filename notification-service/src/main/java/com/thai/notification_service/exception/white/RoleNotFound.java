package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class RoleNotFound extends WhiteException {
    private static final String code = "RNF";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public RoleNotFound(String roleName) {
        super(code, "No role " + roleName, status);
    }
}
