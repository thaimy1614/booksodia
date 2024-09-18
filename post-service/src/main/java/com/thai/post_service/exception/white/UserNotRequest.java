package com.thai.post_service.exception.white;

import org.springframework.http.HttpStatus;

public class UserNotRequest extends WhiteException {
    private static final String code = "UNR";
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotRequest(Long userId) {
        super(code, "User id " + userId + " doesn't have any request", status);
    }

    public UserNotRequest(String msg) {
        super(code, msg, status);
    }
}
