package com.begin.bg.dto.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendOtp {
    private String topic;
    private String email;
    private String otp;
}
