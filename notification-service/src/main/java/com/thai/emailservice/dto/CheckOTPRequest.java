package com.thai.emailservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckOTPRequest {
    private String email;
    private String otp;
}
