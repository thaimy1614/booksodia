package com.thai.post_service.repository;

import com.thai.post_service.model.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, String> {
}
