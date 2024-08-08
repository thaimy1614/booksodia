package com.thai.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarningResponseDto {
    private Long courseId;
    private Long amount;
    private Instant createAt;
}
