package com.thai.emailservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyAccount {
    private String url;
    private String email;
    private String fullName;
}
