package com.thai.profile.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import fsa.cursus.user_service.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponseDto extends UserResponseDto {
    @JsonProperty(value = "nCert")
    private Integer nCert;

    public StudentResponseDto() {
        super(Role.STUDENT_VALUE);
    }
}
