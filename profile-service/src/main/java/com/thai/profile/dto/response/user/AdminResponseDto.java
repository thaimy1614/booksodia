package com.thai.profile.dto.response.user;

import com.thai.profile.model.Role;

public class AdminResponseDto extends UserResponseDto {
    public AdminResponseDto() {
        super(Role.ADMIN_VALUE);
    }
}
