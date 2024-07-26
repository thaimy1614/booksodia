package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class VerificationLinkCorrupted extends WhiteException {
    private static final String code = "VLC";
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public VerificationLinkCorrupted() {
        super(code, "Verification link is modified", status);
    }
}
