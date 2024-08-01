package com.thai.post_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String content; // Text content of the comment

    private LocalDateTime createdAt; // Creation timestamp

    private Long userId; // ID of the user who made the comment

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Reference to the post

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<CommentReaction> reactions; // Reactions on the comment

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
