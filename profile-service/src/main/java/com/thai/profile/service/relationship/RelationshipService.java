package com.thai.profile.service.relationship;

import com.thai.profile.model.Relationship;

public interface RelationshipService {
    Relationship.RelationshipType getRelationship(String firstUser, String secondUser);
}
