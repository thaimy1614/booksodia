package com.thai.post_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {
    public static final String codePrefix = "USER";
    public static final String defaultCode = "UNKNOWN";
    public static final HttpStatus defaultStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String defaultMsg = "Something went wrong, please contact us if the problem persist";

    private final String code;
    private final HttpStatus status;

    public BaseException() {
        this(defaultMsg);
    }

    public BaseException(String message) {
        this(defaultCode, message);
    }

    public BaseException(String code, String message) {
        this(code, message, defaultStatus);
    }

    public BaseException(String code, String message, HttpStatus status) {
        this(code, message, status, null);
    }

    public BaseException(String code, String message, HttpStatus status, Throwable e) {
        super(message, e);
        this.code = createCode(code);
        this.status = status;
    }

    public static String createCode() {
        return createCode(defaultCode);
    }

    public static String createCode(String code) {
        return codePrefix + "_" + code;
    }
}
