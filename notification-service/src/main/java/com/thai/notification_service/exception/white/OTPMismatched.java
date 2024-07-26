package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class OTPMismatched extends WhiteException {
    private static final String code = "OM";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public OTPMismatched() {
        super(code, "Incorrect OTP", status);
    }
}
