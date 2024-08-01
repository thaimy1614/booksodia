package com.thai.post_service.model;

import jakarta.persistence.*;

@Entity
public class CommentReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment; // Reference to the comment

    private Long userId; // ID of the user who reacted

    @Enumerated(EnumType.STRING)
    private ReactionType type; // Reaction type: LIKE, LOVE, ANGRY, etc.

    // Getters and setters
}
