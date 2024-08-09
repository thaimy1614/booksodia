package com.thai.profile.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Locale;

@Getter
@Setter
public abstract class UserResponseDto implements Serializable {
    @JsonProperty("role")
    private final String role;

    @JsonProperty("userId")
    protected String userId;

    @JsonProperty("fullName")
    protected String fullName;

    @JsonProperty("email")
    protected String email;

    @JsonProperty("image")
    protected String image;

    @JsonProperty("isActive")
    protected Boolean isActive;

    @JsonProperty("title")
    protected String title;

    @JsonProperty(value = "nFollowing")
    protected Long nFollowing;

    public UserResponseDto(String role) {
        this.role = role.toUpperCase(Locale.ROOT);
    }
}
