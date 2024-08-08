package com.thai.profile.service.user;

import fsa.cursus.user_service.dto.CheckUserIds;
import fsa.cursus.user_service.dto.mail.UserInfo;
import fsa.cursus.user_service.dto.request.*;
import fsa.cursus.user_service.dto.response.InstructorRegisterResponse;
import fsa.cursus.user_service.dto.response.RegisterResponse;
import fsa.cursus.user_service.dto.response.user.*;
import fsa.cursus.user_service.model.Role;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDto getUserById(Long id);

    UserResponseDto getUserByEmail(String email);

    ListUserResponseDto getAllUsers(Pageable pageable);

    ListUserResponseDto getAllUsers(Pageable pageable, String roleName, @Nullable Boolean isActive);

    UserResponseDto updateUser(Long userId, UpdateUserDto updateUserDto);

    UserResponseDto saveUser(UserRequestDto userDto);

    InstructorRegisterResponse instructorRegistration(Long userId, MultipartFile file, RegisterRequest request);

    BlockUserResponse blockUser(BlockUserRequest request);

    UnblockUserResponse unblockUser(UnblockUserRequest request);

    UserCountResponseDto count(@Nullable Boolean isActive, @Nullable Role.Value role);

    InstructorRegisterResponse approveInstructor(ApproveInstructorRequest request);

    InstructorRegisterResponse rejectInstructor(RejectInstructorRequest request);

    List<RegisterResponse> getAllRegisterRequest();

    String getUserFullName(Long userId);

    Map<Long, UserInfo> createEnrollmentAndEarning(PaymentCompleteDto paymentCompleteDto);

    Boolean checkUserIds(CheckUserIds input);
}
