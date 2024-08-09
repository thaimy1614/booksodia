package com.thai.profile.service.follow;

import com.thai.profile.dto.response.follow.ListFollowerResponse;
import com.thai.profile.dto.response.follow.ListFollowingResponse;

public interface FollowService {
    void followUser(String followerId, String followingId);

    void unfollowUser(String followerId, String followingId);

    ListFollowingResponse getAllFollowing(String userId);

    long countFollowing(String userId);

    ListFollowerResponse getAllFollower(String userId);

    long countFollower(String userId);

    boolean isFollowBy(String currentUser, String following);
}
