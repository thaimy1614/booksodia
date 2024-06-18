package com.begin.bg.mapper;

import com.begin.bg.dto.request.ProfileCreationRequest;
import com.begin.bg.dto.request.UserRequest;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface ProfileCreationMapper {
    ProfileCreationRequest toProfileCreationRequest(UserRequest user);
}
