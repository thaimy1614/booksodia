package com.thai.profile.controller;

import com.thai.profile.model.Relationship;
import com.thai.profile.service.relationship.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}/relationship")
public class RelationShipController {
    private final RelationshipService relationshipService;

    @GetMapping("/{firstUser}/{secondUser}")
    public Relationship.RelationshipType getRelationship(
            @PathVariable("firstUser") String firstUser,
            @PathVariable("secondUser") String secondUser
    ) {
        return relationshipService.getRelationship(firstUser, secondUser);
    }
}
