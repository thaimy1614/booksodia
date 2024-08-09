package com.thai.profile.repository;

import com.thai.profile.model.Follow;
import com.thai.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerAndFollowing(User follower, User following);

    List<Follow> findByFollower(User follower);

    Long countByFollower(User follower);

    List<Follow> findByFollowing(User following);

    Long countByFollowing(User following);

    boolean existsByFollower_UserIdAndFollowing_UserId(String follower, String following);
}
