package com.thai.profile.repository;

import com.thai.profile.model.User;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findByUserEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(DISTINCT u.userId) " +
            "FROM User u JOIN u.roles r " +
            "WHERE (u.isActive = :countBlock) AND r.roleName LIKE :roleName")
    long countByStatusAndRole(boolean countBlock, @Nullable @Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName")
    Page<User> findAllByRoleName(Pageable pageable, @Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleName = :roleName AND u.isActive = :isActive")
    Page<User> findAllByRoleNameAndIsActive(
            Pageable pageable,
            @Param("roleName") String roleName,
            @Param("isActive") boolean isActive
    );

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_name) VALUES (:userId, :roleName)", nativeQuery = true)
    void insertNewRoleForUser(@Param("userId") Long userId, @Param("roleName") String roleName);
}
