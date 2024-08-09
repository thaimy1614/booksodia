package com.thai.profile.controller;

import com.thai.profile.dto.ResponseObject;
import com.thai.profile.dto.request.BlockUserRequest;
import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.request.UnblockUserRequest;
import com.thai.profile.dto.request.UpdateUserDto;
import com.thai.profile.dto.response.user.*;
import com.thai.profile.model.Role;
import com.thai.profile.service.user.UserService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get a user with id {userId}")
    @GetMapping("/{userId}")
    public ResponseObject<UserResponseDto> getUserById(@PathVariable String userId) {
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
    public ResponseObject<UserResponseDto> updateUser(@PathVariable String userId,
                                                      @RequestBody UpdateUserDto updatedUser) {
        UserResponseDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseObject.success(userDto);
    }

    @Hidden     // internal API; called by Auth
    @PostMapping
    public Boolean createUser(
            @Valid @RequestBody ProfileCreationRequest user
    ) {
        UserResponseDto userDto = userService.saveUser(user);
        return userDto != null;
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
    public String getFullName(@RequestParam String userId) {
        return userService.getUserById(userId).getFullName();
    }
}
