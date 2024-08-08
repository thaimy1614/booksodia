package com.thai.post_service.model;

import jakarta.persistence.*;

@Entity
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment; // Reference to the comment

    @Column(name = "user_id")
    private Long userId; // ID of the user who reacted

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    public enum ReactionType {
        LIKE, LOVE, ANGRY, HAHA, SAD, WOW
    }
}
