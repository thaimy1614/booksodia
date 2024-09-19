package com.thai.post_service.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content;

    @Column(name = "media_url")
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "user_id")
    private String userId; // ID of the user who created the post

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

    public enum PostType {
        TEXT, IMAGE, VIDEO, LINK
    }

    public enum Visibility {
        PUBLIC, FRIENDS_ONLY, PRIVATE
    }
}
