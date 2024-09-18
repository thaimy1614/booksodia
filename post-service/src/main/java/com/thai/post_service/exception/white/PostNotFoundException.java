package com.thai.post_service.exception.white;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends WhiteException {
    private static final String code = "PNF";
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public PostNotFoundException(String userId) {
        super(code, "Post not found with id " + userId, status);
    }

    public PostNotFoundException(String userId, String msg) {
        super(code, msg, status);
    }
}
