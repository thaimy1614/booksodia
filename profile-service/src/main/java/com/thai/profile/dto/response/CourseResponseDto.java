package com.thai.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDto {
    private String title;
    private String category;
    private String instructor;
    private long view = 10000L;
    private Timestamp createAt = Timestamp.valueOf(LocalDateTime.now());
}
