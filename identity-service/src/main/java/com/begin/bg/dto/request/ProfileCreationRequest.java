package com.begin.bg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationRequest {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
    private String avatar;
}
