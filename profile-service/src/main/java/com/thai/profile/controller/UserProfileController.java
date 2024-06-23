package com.thai.profile.controller;

import com.thai.profile.dto.ResponseObject;
import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.response.UserProfileResponse;
import com.thai.profile.entity.UserProfile;
import com.thai.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/users/my-info")
    ResponseEntity<ResponseObject> getMyInfo(){
        UserProfile profile = userProfileService.getMyInfo();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "GET YOUR INFORMATION SUCCESSFUL!", profile));
    }

    @GetMapping("/users/{profileId}")
    ResponseEntity<UserProfileResponse> getProfile(@PathVariable String profileId){
        return ResponseEntity.ok(userProfileService.getProfile(profileId));
    }

    @GetMapping("/users")
    ResponseEntity<List<UserProfile>> getAll(){
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.getAll());
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
