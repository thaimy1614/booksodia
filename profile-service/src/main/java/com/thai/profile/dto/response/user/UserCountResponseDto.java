package com.thai.profile.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCountResponseDto {
    private long count;
}
