package com.thai.notification_service.dto;

import com.thai.notification_service.exception.handler.ExceptionDTO;

public record ResponseObject<T>(T data, ExceptionDTO error) {
    public static <T> ResponseObject<T> success(T data) {
        return new ResponseObject<>(data, null);
    }

    public static <T> ResponseObject<T> error(ExceptionDTO error) {
        return new ResponseObject<>(null, error);
    }
}