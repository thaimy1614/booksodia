package com.thai.notification_service.exception.white;

import org.springframework.http.HttpStatus;

public class OTPExpired extends WhiteException {
    private static final String code = "OE";
    private static final HttpStatus status = HttpStatus.FORBIDDEN;

    public OTPExpired() {
        super(code, "OTP code expired", status);
    }
}
