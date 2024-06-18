package com.begin.bg.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationResponse {
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
}
