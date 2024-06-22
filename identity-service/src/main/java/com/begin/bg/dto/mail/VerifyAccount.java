package com.begin.bg.dto.mail;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyAccount {
    private String url;
    private String email;
    private String fullName;
}
