package com.thai.profile.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thai.profile.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNormalResponseDto extends UserResponseDto {
    @JsonProperty(value = "nFollower")
    private Long nFollower;

    public UserNormalResponseDto() {
        super(Role.USER_VALUE);
    }
}
