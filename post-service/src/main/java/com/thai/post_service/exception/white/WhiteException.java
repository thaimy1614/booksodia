package com.thai.post_service.exception.white;

import com.thai.post_service.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Exception that will display the message to the user
 */
public abstract class WhiteException extends BaseException {
    private static final String codePrefix = "WH";

    public WhiteException(String code, String message, HttpStatus status) {
        this(code, message, status, null);
    }

    public WhiteException(String code, String message, HttpStatus status, Throwable e) {
        super(createCode(code), message, status, e);
    }

    public static String createCode(String code) {
        return codePrefix + "_" + code;
    }
}
