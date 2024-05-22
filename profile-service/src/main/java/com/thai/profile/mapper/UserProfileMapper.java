package com.thai.profile.mapper;

import com.thai.profile.dto.request.ProfileCreationRequest;
import com.thai.profile.dto.response.UserProfileResponse;
import com.thai.profile.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
}
