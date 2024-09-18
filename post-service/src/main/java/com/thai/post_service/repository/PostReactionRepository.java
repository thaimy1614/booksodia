package com.thai.post_service.repository;

import com.thai.post_service.model.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReactionRepository extends JpaRepository<PostReaction, String> {
}
