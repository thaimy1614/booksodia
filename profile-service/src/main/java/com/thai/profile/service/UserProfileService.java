package com.thai.profile.service;

import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.response.UserProfileResponse;
import com.thai.profile.entity.UserProfile;
import com.thai.profile.mapper.UserProfileMapper;
import com.thai.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = UserProfile.builder()
                .userId(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .avatar(request.getAvatar())
                .city(request.getCity())
                .dob(request.getDob())
                .build();
        userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public UserProfileResponse getProfile(String profileId){
        UserProfile userProfile = userProfileRepository.findById(profileId).orElseThrow(()->new RuntimeException("Profile not found!"));
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse updateProfile(ProfileCreationRequest request, String id) {
        UserProfile userProfile = userProfileRepository.findById(id).map(profile-> {
            profile = UserProfile
                    .builder()
                    .id(id)
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dob(request.getDob())
                    .city(request.getCity())
                    .build();
            return profile;

        }).orElseThrow(() -> new RuntimeException("Profile not found!"));
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    public List<UserProfile> getAll() {
        return userProfileRepository.findAll();
    }

    @PostAuthorize("returnObject.userId == authentication.name")
    public UserProfile getMyInfo() {
        var user = SecurityContextHolder.getContext().getAuthentication();
        String email = user.getName();
        return userProfileRepository.findByUserId(email).get();
    }
}
