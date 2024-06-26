package com.begin.bg.mapper;

import com.begin.bg.dto.request.ProfileCreationRequest;
import com.begin.bg.dto.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileCreationMapper {
    ProfileCreationRequest toProfileCreationRequest(UserRequest user);
}
