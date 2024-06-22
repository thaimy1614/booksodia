package com.begin.bg.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyAccountResponse {
    private boolean success;
}
