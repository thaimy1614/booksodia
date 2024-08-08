package com.thai.profile.service.follow;

import com.thai.profile.dto.response.follow.ListFollowerResponse;
import com.thai.profile.dto.response.follow.ListFollowingResponse;

public interface FollowService {
    void followUser(Long followerId, Long followingId);

    void unfollowUser(Long followerId, Long followingId);

    ListFollowingResponse getAllFollowing(Long userId);

    long countFollowing(Long userId);

    ListFollowerResponse getAllFollower(Long userId);

    long countFollower(Long userId);

    boolean isFollowBy(long currentUser, Long following);
}
