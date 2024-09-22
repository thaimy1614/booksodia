package com.thai.profile.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreationRequest {
    @NotNull(message = "Id is required")
    private String userId;

    @NotEmpty(message = "FullName is required")
    private String fullName;

    @JsonProperty(defaultValue = "false")
    private Boolean isAdmin;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email is not valid")
    private String email;

    private String image;
}
