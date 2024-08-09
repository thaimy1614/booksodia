package com.thai.profile.service.follow;

import com.thai.profile.dto.response.follow.FollowerResponse;
import com.thai.profile.dto.response.follow.FollowingResponse;
import com.thai.profile.dto.response.follow.ListFollowerResponse;
import com.thai.profile.dto.response.follow.ListFollowingResponse;
import com.thai.profile.exception.white.UserNotFoundException;
import com.thai.profile.model.Follow;
import com.thai.profile.model.User;
import com.thai.profile.repository.FollowRepository;
import com.thai.profile.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#followingId")
    public void followUser(String followerId, String followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following not found"));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);

        followRepository.save(follow);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#followingId")
    public void unfollowUser(String followerId, String followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserNotFoundException("Follower not found"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new UserNotFoundException("Following not found"));

        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public ListFollowingResponse getAllFollowing(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<FollowingResponse> followingUsers = followRepository.findByFollower(user).stream()
                .map(Follow::getFollowing)
                .map(following -> FollowingResponse
                        .builder()
                        .id(following.getUserId())
                        .title(following.getTitle())
                        .name(following.getFullName())
                        .image(following.getImage())
                        .nFollower(following.getNFollower())
                        .build()
                )
                .toList();

        return ListFollowingResponse.builder()
                .followings(followingUsers)
                .build();
    }

    @Override
    public long countFollowing(String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        final User user = userRepository.getReferenceById(userId);

        return followRepository.countByFollower(user);
    }

    @Override
    public ListFollowerResponse getAllFollower(String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);

        User user = userRepository.getReferenceById(userId);
        List<FollowerResponse> followers = followRepository.findByFollowing(user)
                .stream()
                .map(Follow::getFollower)
                .map(follower -> FollowerResponse
                        .builder()
                        .id(follower.getUserId())
                        .name(follower.getFullName())
                        .image(follower.getImage())
                        .title(follower.getTitle())
                        .nFollower(follower.getNFollower())
                        .build()
                )
                .toList();

        return ListFollowerResponse.builder()
                .followers(followers)
                .build();
    }

    @Override
    public long countFollower(String userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
        final User user = userRepository.getReferenceById(userId);

        return followRepository.countByFollowing(user);
    }

    @Override
    public boolean isFollowBy(String currentUser, String following) {
        return followRepository.existsByFollower_UserIdAndFollowing_UserId(currentUser, following);
    }
}
