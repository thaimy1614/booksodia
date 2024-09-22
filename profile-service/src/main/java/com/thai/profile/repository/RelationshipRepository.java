package com.thai.profile.repository;

import com.thai.profile.model.Relationship;
import com.thai.profile.model.RelationshipId;
import com.thai.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipId> {
    Optional<Relationship> findByFirstUserAndSecondUser(User firstUser, User secondUser);

    Optional<Relationship> findBySecondUserAndFirstUser(User secondUser, User firstUser);

}
