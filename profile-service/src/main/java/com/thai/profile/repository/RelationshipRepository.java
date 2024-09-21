package com.thai.profile.repository;

import com.thai.profile.model.Relationship;
import com.thai.profile.model.RelationshipId;
import com.thai.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {
    Relationship findByFirstUserAndSecondUser(User firstUser, User secondUser);
}
