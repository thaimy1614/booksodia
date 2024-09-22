package com.thai.profile.dto.response.relationship;

import com.thai.profile.model.Relationship;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RelationshipResponse {
    private String myId;
    private String theirId;
    private Relationship.RelationshipType relationshipType;
}
