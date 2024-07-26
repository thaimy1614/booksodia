package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class VerificationLinkExpired extends WhiteException {
    private static final String code = "VLE";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public VerificationLinkExpired() {
        super(code, "Verification link expired, new link is sent!", status);
    }
}
