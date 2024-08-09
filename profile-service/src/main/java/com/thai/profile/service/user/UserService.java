package com.thai.profile.service.user;

import com.thai.profile.dto.request.BlockUserRequest;
import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.request.UnblockUserRequest;
import com.thai.profile.dto.request.UpdateUserDto;
import com.thai.profile.dto.response.user.*;
import com.thai.profile.model.Role;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto getUserById(String id);

    UserResponseDto getUserByEmail(String email);

    ListUserResponseDto getAllUsers(Pageable pageable);

    ListUserResponseDto getAllUsers(Pageable pageable, String roleName, @Nullable Boolean isActive);

    UserResponseDto updateUser(String userId, UpdateUserDto updateUserDto);

    UserResponseDto saveUser(ProfileCreationRequest userDto);

    BlockUserResponse blockUser(BlockUserRequest request);

    UnblockUserResponse unblockUser(UnblockUserRequest request);

    UserCountResponseDto count(@Nullable Boolean isActive, @Nullable Role.Value role);
}
