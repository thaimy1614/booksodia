package com.thai.post_service.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content; // Text content of the post

    private String mediaUrl; // URL for media content like images or videos

    @Enumerated(EnumType.STRING)
    private PostType postType; // TEXT, IMAGE, VIDEO, LINK

    private LocalDateTime createdAt; // Creation timestamp

    private LocalDateTime updatedAt; // Last updated timestamp

    @Enumerated(EnumType.STRING)
    private Visibility visibility; // PUBLIC, FRIENDS_ONLY, PRIVATE

    private Long userId; // ID of the user who created the post

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments; // List of comments on the post

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostReaction> reactions; // List of reactions on the post

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
