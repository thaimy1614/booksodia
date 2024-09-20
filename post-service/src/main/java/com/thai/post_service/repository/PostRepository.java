package com.thai.post_service.repository;

import com.thai.post_service.model.Post;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {
    @Nonnull
    Page<Post> findAll(@Nonnull Pageable pageable);

    Page<Post> findAllByUserId(String userId, Pageable pageable);
}
