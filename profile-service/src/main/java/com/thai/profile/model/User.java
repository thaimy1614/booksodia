package com.thai.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_active = FALSE WHERE user_id=?")
public class User {
    public static final User DEFAULT_USER = User.builder()
            .userId(0)
            .fullName("Unknown or Removed User")
            .image(null)
            .isActive(false)
            .roles(Set.of())
            .build();

    @Id
    @EqualsAndHashCode.Include
    private String userId;

    @EqualsAndHashCode.Include
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "image")
    private String image;

    @Column(name = "title")
    private String title;

    @Column(name = "about")
    private String about;

    @Builder.Default
    @Column(name = "n_follower", nullable = false)
    private Long nFollower = 0L;

    @Builder.Default
    @Column(name = "n_following", nullable = false)
    private Long nFollowing = 0L;

    @CreationTimestamp
    @Column(name = "create_at", insertable = false, updatable = false)
    private Timestamp createAt = Timestamp.valueOf(LocalDateTime.now());

    @UpdateTimestamp
    @Column(name = "update_at", insertable = false)
    private Timestamp updateAt = Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_name")
    )

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Follow> following = new HashSet<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Follow> followers = new HashSet<>();

    public static String simplifyRoles(Set<Role> roles) {
        if (roles.contains(Role.ADMIN)) {
            return Role.ADMIN_VALUE;
        } else if (roles.contains(Role.INSTRUCTOR)) {
            return Role.INSTRUCTOR_VALUE;
        } else {
            return Role.STUDENT_VALUE;
        }
    }
}
