package com.thai.profile.dto.request;

import com.thai.profile.model.Relationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationshipRequest {
    private Relationship.RelationshipType relationshipType;
}
