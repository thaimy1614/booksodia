package com.thai.profile.controller;

import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.response.UserProfileResponse;
import com.thai.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/users/{profileId}")
    ResponseEntity<UserProfileResponse> getProfile(@PathVariable String profileId){
        return ResponseEntity.ok(userProfileService.getProfile(profileId));
    }

    @PostMapping("/users")
    ResponseEntity<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createProfile(request));
    }

    @PutMapping("/users/{profileId}")
    ResponseEntity<UserProfileResponse> updateProfile(@RequestBody ProfileCreationRequest request, @PathVariable String profileId){
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.updateProfile(request, profileId));
    }
}
