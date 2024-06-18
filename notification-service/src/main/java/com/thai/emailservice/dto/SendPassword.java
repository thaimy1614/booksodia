package com.thai.emailservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendPassword {
    private String topic;
    private String email;
    private String password;
}
