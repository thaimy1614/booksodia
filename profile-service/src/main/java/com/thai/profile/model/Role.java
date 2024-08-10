package com.thai.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {
    public static final String ADMIN_VALUE = "ADMIN";
    public static final Role ADMIN = new Role(ADMIN_VALUE);
    public static final String USER_VALUE = "USER";
    public static final Role USER = new Role(USER_VALUE);

    @Id
    @Nonnull
    @EqualsAndHashCode.Include
    @Column(name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public enum Value {
        admin, user
    }
}
