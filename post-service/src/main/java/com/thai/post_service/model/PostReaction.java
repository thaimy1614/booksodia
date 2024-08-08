package com.thai.post_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    public enum ReactionType {
        LIKE, LOVE, ANGRY, HAHA, SAD, WOW
    }
}
