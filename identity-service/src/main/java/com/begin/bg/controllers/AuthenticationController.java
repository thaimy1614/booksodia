package com.begin.bg.controllers;


import com.begin.bg.dto.request.*;
import com.begin.bg.dto.response.ChangePasswordResponse;
import com.begin.bg.dto.response.IntrospectResponse;
import com.begin.bg.dto.response.SendOTPResponse;
import com.begin.bg.entities.ResponseObject;
import com.begin.bg.entities.User;
import com.begin.bg.enums.UserStatus;
import com.begin.bg.mapper.ProfileCreationMapper;
import com.begin.bg.repositories.PermissionRepository;
import com.begin.bg.repositories.RoleRepository;
import com.begin.bg.repositories.httpclient.ProfileClient;
import com.begin.bg.services.AuthenticationService;
import com.begin.bg.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final ProfileClient profileClient;
    private final ProfileCreationMapper mapper;
    //Insert new User with POST method
    @PostMapping("/signup")
    ResponseEntity<ResponseObject> insertUser(@RequestBody UserRequest newUser) {
//        log.info("Create user");
        Optional<User> foundUser = userService.findUserByName(newUser.getEmail());
        if (foundUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new ResponseObject("FAIL", "User name already taken", null));
        } else {
            var roleNameList = newUser.getRoles();
            var roles = roleRepository.findAllById(roleNameList);
            if (roles.size() != roleNameList.size()) {
                // Some roles were not found
                // Handle the scenario where some roles were not found
                // For example, return an error response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseObject("FAIL", "Some roles not found", null));
            }
            User user = User.builder()
                    .email(newUser.getEmail())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .status(UserStatus.UNVERIFIED.name())
                    .roles(new HashSet<>(roles))
                    .build();
            System.out.println(roleNameList);
            System.out.println(roles);
            user = userService.saveUser(user);
            var profileRequest = mapper.toProfileCreationRequest(newUser);
            profileRequest.setUserId(user.getEmail().toString());
            var profileResponse = profileClient.createProfile(profileRequest);
//            log.info(profileResponse.toString());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("OK", "Insert User successful!", user));
        }
    }


    @PostMapping("/auth")
    ResponseEntity<ResponseObject> authenticate(@RequestBody User user) throws Exception {
        var auth = authService.authenticate(user);
        return auth != null ? (ResponseEntity.status(HttpStatus.OK).body(auth)) :
                (ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject(
                        "FAILED", "Username or password not correct!", null)))
                ;
    }

    @PostMapping("/introspect")
    ResponseEntity<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest) throws Exception {
        IntrospectResponse introspect = authService.introspect(introspectRequest.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(new IntrospectResponse(introspect.isValid()));
    }

    @GetMapping("/log-out")
    ResponseEntity<ResponseObject> logout(@RequestBody InvalidatedTokenRequest invalidatedTokenRequest) throws Exception {
        authService.logout(invalidatedTokenRequest.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Logout successful!", invalidatedTokenRequest
                .getToken()));
    }

    @GetMapping("/refresh")
    ResponseEntity<ResponseObject> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception {
        String token = authService.refreshToken(request);
        return ResponseEntity.ok(new ResponseObject("OK", "Refresh token successful!", token));
    }

    @PostMapping("/change-password")
    ResponseEntity<ResponseObject> changePassword(@RequestBody ChangePasswordRequest request) throws Exception {
        ChangePasswordResponse response = authService.changePassword(request);
        return ResponseEntity.ok().body(new ResponseObject("OK", "Change password successful!", response));
    }

    @PostMapping("/forget-password/send-otp")
    ResponseEntity<ResponseObject> sendOtp(@RequestBody SendOTPRequest request){
        SendOTPResponse response = authService.sendOTPForForgetPassword(request);
        return ResponseEntity.ok().body(new ResponseObject("OK", "Send OTP successful!", response));
    }
}
