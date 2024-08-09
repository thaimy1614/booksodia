package com.begin.bg.mapper;

import com.begin.bg.dto.request.ProfileCreationRequest;
import com.begin.bg.dto.request.UserRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class ProfileCreationMapperImpl implements ProfileCreationMapper {

    @Override
    public ProfileCreationRequest toProfileCreationRequest(UserRequest user) {
        if ( user == null ) {
            return null;
        }

        ProfileCreationRequest.ProfileCreationRequestBuilder profileCreationRequest = ProfileCreationRequest.builder();

        profileCreationRequest.email( user.getEmail() );
        profileCreationRequest.fullName( user.getFullName() );
        profileCreationRequest.image( user.getImage() );

        return profileCreationRequest.build();
    }
}
