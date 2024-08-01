package com.thai.post_service.model;


import jakarta.persistence.*;

@Entity
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // Reference to the post

    private Long userId; // ID of the user who reacted

    @Enumerated(EnumType.STRING)
    private ReactionType type; // Reaction type: LIKE, LOVE, ANGRY, etc.

    // Getters and setters
}
