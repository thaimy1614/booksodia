package com.thai.profile.service.relationship;

import com.thai.profile.exception.white.UserNotFoundException;
import com.thai.profile.model.Relationship;
import com.thai.profile.model.RelationshipId;
import com.thai.profile.model.User;
import com.thai.profile.repository.RelationshipRepository;
import com.thai.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelationshipImpl implements RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    @Override
    public Relationship.RelationshipType getRelationship(String firstUser, String secondUser) {
        User user1 = userRepository.findByUserId(firstUser).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user2 = userRepository.findByUserId(secondUser).orElseThrow(() -> new UserNotFoundException("User not found"));
        Relationship relationship = relationshipRepository.findByFirstUserAndSecondUser(user1, user2);
        if (relationship == null) {
            return Relationship.RelationshipType.STRANGER;
        }else{
            return relationship.getRelationshipType();
        }
    }
}
