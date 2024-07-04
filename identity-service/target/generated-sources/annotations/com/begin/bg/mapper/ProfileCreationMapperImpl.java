package com.begin.bg.mapper;

import com.begin.bg.dto.request.ProfileCreationRequest;
import com.begin.bg.dto.request.UserRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class ProfileCreationMapperImpl implements ProfileCreationMapper {

    @Override
    public ProfileCreationRequest toProfileCreationRequest(UserRequest user) {
        if ( user == null ) {
            return null;
        }

        ProfileCreationRequest.ProfileCreationRequestBuilder profileCreationRequest = ProfileCreationRequest.builder();

        profileCreationRequest.firstName( user.getFirstName() );
        profileCreationRequest.lastName( user.getLastName() );
        profileCreationRequest.dob( user.getDob() );
        profileCreationRequest.city( user.getCity() );

        return profileCreationRequest.build();
    }
}
