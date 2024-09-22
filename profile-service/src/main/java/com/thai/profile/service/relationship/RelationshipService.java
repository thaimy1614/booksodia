package com.thai.profile.service.relationship;

import com.thai.profile.dto.response.relationship.RelationshipResponse;
import com.thai.profile.model.Relationship;

public interface RelationshipService {
    Relationship.RelationshipType getRelationship(String firstUser, String secondUser);

    RelationshipResponse makeRelationship(String myId, String theirId, Relationship.RelationshipType type);
}
