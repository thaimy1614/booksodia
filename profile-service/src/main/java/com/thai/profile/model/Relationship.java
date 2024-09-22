package com.thai.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RelationshipId.class)
public class Relationship {
    @Id
    @ManyToOne
    @JoinColumn(name = "first_user_id", nullable = false)
    private User firstUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "second_user_id", nullable = false)
    private User secondUser;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum RelationshipType {
        FRIEND, BLOCKED, STRANGER
    }
}
