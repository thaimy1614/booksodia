package com.thai.profile.mapper;

import com.thai.profile.dto.mail.UserInfo;
import com.thai.profile.dto.request.UserRequestDto;
import com.thai.profile.dto.response.user.AdminResponseDto;
import com.thai.profile.dto.response.user.UserNormalResponseDto;
import com.thai.profile.dto.response.user.UserResponseDto;
import com.thai.profile.model.Role;
import com.thai.profile.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserMapper {
    public UserResponseDto userToUserResponseDto(User user) {
        String currentUserRole = User.simplifyRoles(user.getRoles());
        return switch (currentUserRole) {
            case (Role.ADMIN_VALUE) -> userToAdminResponseDto(user);
            case (Role.USER_VALUE) -> userToUserNormalResponseDto(user);
            default -> throw new RuntimeException("Unknown role: " + currentUserRole);
        };
    }

    public abstract User userRequestDtoToUser(UserRequestDto userRequestDto);

    public abstract AdminResponseDto userToAdminResponseDto(User user);

    public abstract UserNormalResponseDto userToUserNormalResponseDto(User user);

    public abstract UserInfo userToUserInfo(User user);
}
