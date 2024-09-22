package com.thai.profile.controller;

import com.thai.profile.dto.ResponseObject;
import com.thai.profile.dto.request.RelationshipRequest;
import com.thai.profile.dto.response.relationship.RelationshipResponse;
import com.thai.profile.model.Relationship;
import com.thai.profile.service.relationship.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}/relationship")
public class RelationShipController {
    private final RelationshipService relationshipService;

    @PostMapping("/{userId}")
    ResponseObject<RelationshipResponse> makeRelationship(
            @PathVariable String userId,
            @RequestBody RelationshipRequest relationshipRequest,
            JwtAuthenticationToken token
    ) {
        RelationshipResponse response = relationshipService.makeRelationship(token.getName(), userId, relationshipRequest.getRelationshipType());
        return ResponseObject.success(response);
    }

    @GetMapping("/{firstUser}/{secondUser}")
    public Relationship.RelationshipType getRelationship(
            @PathVariable("firstUser") String firstUser,
            @PathVariable("secondUser") String secondUser
    ) {
        return relationshipService.getRelationship(firstUser, secondUser);
    }
}
