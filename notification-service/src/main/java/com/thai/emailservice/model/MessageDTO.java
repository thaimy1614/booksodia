package com.thai.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;
    private String topic;
    private String to;
    private String from;
}
