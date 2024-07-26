package com.thai.notification_service.exception.black;

import com.thai.notification_service.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception that is hidden to the user
 */
public abstract class BlackException extends BaseException {
    private static final String codePrefix = "BL";
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public BlackException(String code, String message, HttpStatus status) {
        super(createCode(code), message, status);
    }

    public BlackException(String code, String message, HttpStatus status, Throwable e) {
        super(code, message, status, e);
    }

    public static String createCode(String code) {
        return codePrefix + "_" + code;
    }
}
