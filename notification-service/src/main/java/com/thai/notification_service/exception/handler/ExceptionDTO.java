package com.thai.notification_service.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String code;
    private List<String> message;

    public ExceptionDTO(String code, String message) {
        this.code = code;
        this.message = List.of(message);
    }
}
