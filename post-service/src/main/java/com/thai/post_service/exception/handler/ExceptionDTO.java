package com.thai.post_service.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String code;
    private List<String> message;

    public ExceptionDTO(String code, String messages) {
        this.code = code;
        this.message = new ArrayList<>();
        this.message.add(messages);
    }
}
