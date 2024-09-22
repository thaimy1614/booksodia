package com.thai.profile.dto.request;

import com.thai.profile.model.Relationship;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelationshipRequest {
    private Relationship.RelationshipType relationshipType;
}
