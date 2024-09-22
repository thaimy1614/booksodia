package com.thai.profile.service.relationship;

import com.thai.profile.dto.response.relationship.RelationshipResponse;
import com.thai.profile.exception.white.UserNotFoundException;
import com.thai.profile.model.Relationship;
import com.thai.profile.model.RelationshipId;
import com.thai.profile.model.User;
import com.thai.profile.repository.RelationshipRepository;
import com.thai.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelationshipImpl implements RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    @Override
    public Relationship.RelationshipType getRelationship(String firstUser, String secondUser) {
        User user1 = userRepository.findByUserId(firstUser).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user2 = userRepository.findByUserId(secondUser).orElseThrow(() -> new UserNotFoundException("User not found"));
        RelationshipId id = new RelationshipId(user1, user2);
        Optional<Relationship> relationship = relationshipRepository.findById(id);
        if (relationship.isEmpty()) {
            return Relationship.RelationshipType.STRANGER;
        }else{
            return relationship.get().getRelationshipType();
        }
    }

    @Override
    public RelationshipResponse makeRelationship(String myId, String theirId, Relationship.RelationshipType type) {
        User user1 = userRepository.findByUserId(myId).orElseThrow(() -> new UserNotFoundException("User not found"));
        User user2 = userRepository.findByUserId(theirId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Relationship relationship = new Relationship();
        relationship.setRelationshipType(type);
        relationship.setFirstUser(user1);
        relationship.setSecondUser(user2);
        relationship = relationshipRepository.save(relationship);
        return RelationshipResponse.builder()
                .myId(relationship.getFirstUser().getUserId())
                .theirId(relationship.getSecondUser().getUserId())
                .relationshipType(relationship.getRelationshipType())
                .build();
    }
}
