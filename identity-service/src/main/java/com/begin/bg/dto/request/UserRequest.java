package com.begin.bg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
    private List<String> roles;
}
