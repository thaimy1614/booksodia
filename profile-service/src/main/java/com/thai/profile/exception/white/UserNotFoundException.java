package com.thai.profile.exception.white;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends WhiteException {
    private static final String code = "RNF";
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotFoundException(Long userId) {
        super(code, "User not found with id " + userId, status);
    }

    public UserNotFoundException(String msg) {
        super(code, msg, status);
    }
}
