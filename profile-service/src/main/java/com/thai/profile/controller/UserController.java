package com.thai.profile.controller;

import fsa.cursus.user_service.dto.CheckUserIds;
import fsa.cursus.user_service.dto.ResponseObject;
import fsa.cursus.user_service.dto.mail.UserInfo;
import fsa.cursus.user_service.dto.request.*;
import fsa.cursus.user_service.dto.response.InstructorRegisterResponse;
import fsa.cursus.user_service.dto.response.RegisterResponse;
import fsa.cursus.user_service.dto.response.user.*;
import fsa.cursus.user_service.model.Role;
import fsa.cursus.user_service.service.user.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get a user with id {userId}")
    @GetMapping("/{userId}")
    public ResponseObject<UserResponseDto> getUserById(@PathVariable Long userId) {
        return ResponseObject.success(userService.getUserById(userId));
    }

    @Operation(summary = "Get a user with email {email}")
    @GetMapping("/email/{email}")
    public ResponseObject<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return ResponseObject.success(userService.getUserByEmail(email));
    }

    @Operation(summary = "Get all user of all role")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping
    public ResponseObject<ListUserResponseDto> getAllUsers(
            @ParameterObject @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseObject.success(userService.getAllUsers(pageable));
    }

    @Operation(summary = "Count user based on status and role")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @GetMapping("/count")
    public ResponseObject<UserCountResponseDto> countUser(
            @Parameter(description = "null if want to count both active and inactive")
            @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "null if want to count all role")
            @RequestParam(required = false) Role.Value role
    ) {
        return ResponseObject.success(userService.count(isActive, role));
    }

    @Operation(summary = "Admin: Get all user of role instructor")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/instructor/admin")
    public ResponseObject<ListUserResponseDto> getAllInstructors(
            @Parameter(description = "NULL: active and inactive. True: Active. False: Inactive")
            @RequestParam(required = false) Boolean isActive,
            @ParameterObject @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseObject.success(userService.getAllUsers(pageable, Role.INSTRUCTOR_VALUE, isActive));
    }

    @Operation(summary = "Get all active user of role instructor")
    @GetMapping("/instructor")
    public ResponseObject<ListUserResponseDto> getAllInstructorsAdmin(
            @ParameterObject @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseObject.success(userService.getAllUsers(pageable, Role.INSTRUCTOR_VALUE, true));
    }

    @Operation(summary = "Get all user of role student. Note an Instructor is also a Student")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @GetMapping("/student")
    public ResponseObject<ListUserResponseDto> getAllStudents(
            @Parameter(description = "NULL: active and inactive. True: Active. False: Inactive")
            @RequestParam(required = false) Boolean isActive,
            @ParameterObject @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseObject.success(userService.getAllUsers(pageable, Role.STUDENT_VALUE, isActive));
    }

    @Operation(summary = "Get all user of role admin")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @GetMapping("/admin")
    public ResponseObject<ListUserResponseDto> getAllAdmins(
            @ParameterObject @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseObject.success(userService.getAllUsers(pageable, Role.ADMIN_VALUE, true));
    }

    @Operation(summary = "Update profile of user with id {userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/{userId}")
    public ResponseObject<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody UpdateUserDto updatedUser) {
        UserResponseDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseObject.success(userDto);
    }

    @Operation(summary = "Register to become an instructor")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @PostMapping(value = "/register-instructor")
    public ResponseObject<InstructorRegisterResponse> instructorRegistration(
            @RequestParam List<Long> categoryId,
            @RequestParam String description,
            @RequestParam MultipartFile file
    ) {
        final long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        final RegisterRequest request = new RegisterRequest();
        request.setDescription(description);
        request.setCategoryIds(categoryId);

        final InstructorRegisterResponse response = userService.instructorRegistration(userId, file, request);
        return ResponseObject.success(response);
    }

    @Operation(summary = "Approve instructor registration request")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @PostMapping("/approve-instructor")
    public ResponseObject<InstructorRegisterResponse> approveInstructor(
            @RequestBody ApproveInstructorRequest request
    ) {
        InstructorRegisterResponse response = userService.approveInstructor(request);
        return ResponseObject.success(response);
    }

    @Operation(summary = "Reject instructor registration request")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @PostMapping("/reject-instructor")
    public ResponseObject<InstructorRegisterResponse> rejectInstructor(
            @RequestBody RejectInstructorRequest request
    ) {
        InstructorRegisterResponse response = userService.rejectInstructor(request);
        return ResponseObject.success(response);
    }

    @Operation(summary = "Get all instructor registrations")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @GetMapping("/register-instructor")
    public ResponseObject<List<RegisterResponse>> registerInstructor() {
        List<RegisterResponse> response = userService.getAllRegisterRequest();
        return ResponseObject.success(response);
    }

    @Hidden     // internal API; called by Auth
    @PostMapping
    public ResponseObject<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto user
    ) {
        UserResponseDto userDto = userService.saveUser(user);
        return ResponseObject.success(userDto);
    }

    @Hidden     // internal API; called by Auth
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @PostMapping("/block")
    public BlockUserResponse blockUser(@RequestBody BlockUserRequest request) {
        return userService.blockUser(request);
    }

    @Hidden     // internal API; called by Auth
    @PreAuthorize("hasAuthority('SCP_ADMIN_PERMS')")
    @PostMapping("/unblock")
    public UnblockUserResponse unblockUser(@RequestBody UnblockUserRequest request) {
        return userService.unblockUser(request);
    }

    @Hidden     // internal API; called by Auth and KafkaStream
    @GetMapping("/get-full-name")
    public String getFullName(@RequestParam Long userId) {
        return userService.getUserById(userId).getFullName();
    }

    @Hidden     // internal API; called by Payment
    @PostMapping("/payment")
    public Map<Long, UserInfo> createEnrollment(
            @RequestBody @Valid PaymentCompleteDto paymentCompleteDto
    ) {
        return userService.createEnrollmentAndEarning(paymentCompleteDto);
    }

    @Hidden     // internal API: called by Course
    @GetMapping("/check")
    public Boolean checkUsers(@ParameterObject @RequestParam List<Long> userIdList) {
        CheckUserIds input = CheckUserIds.builder()
                .userIds(userIdList)
                .build();
        return userService.checkUserIds(input);
    }
}
