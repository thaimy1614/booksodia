package com.thai.profile.controller;

import com.thai.profile.dto.ResponseObject;
import com.thai.profile.dto.response.EmptyObject;
import com.thai.profile.dto.response.follow.ListFollowerResponse;
import com.thai.profile.dto.response.follow.ListFollowingResponse;
import com.thai.profile.exception.white.CustomValidationException;
import com.thai.profile.service.follow.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.api-prefix}")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "Get all following")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/following")
    public ResponseObject<ListFollowingResponse> getAllFollowing(
            JwtAuthenticationToken token
    ) {
        String userId = token.getName();
        return ResponseObject.success(followService.getAllFollowing(userId));
    }

    @Operation(summary = "Count number of following")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/following/count")
    public ResponseObject<Long> countFollowing(
            JwtAuthenticationToken token
    ) {
        String userId = token.getName();
        return ResponseObject.success(followService.countFollowing(userId));
    }

    @Operation(summary = "Check if the current log in user follow {userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/following/check")
    public ResponseObject<Boolean> isFollowed(
            @RequestParam String following,
            @Autowired JwtAuthenticationToken token
    ) {
        String currentUser = token.getName();
        final boolean res = followService.isFollowBy(currentUser, following);
        return ResponseObject.success(res);
    }

    @Operation(summary = "Get all follower")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/follower")
    public ResponseObject<ListFollowerResponse> getAllFollower(
            JwtAuthenticationToken token
    ) {
        String userId = token.getName();
        return ResponseObject.success(followService.getAllFollower(userId));
    }

    @Operation(summary = "Count number of followers")
    @GetMapping("/{userId}/follower/count")
    public ResponseObject<Long> countFollower(
            @PathVariable String userId
    ) {
        return ResponseObject.success(followService.countFollower(userId));
    }

    @Operation(summary = "Count number of followers")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/follower/count")
    public ResponseObject<Long> countFollower(
            JwtAuthenticationToken token
    ) {
        String userId = token.getName();
        return ResponseObject.success(followService.countFollower(userId));
    }

    @Operation(summary = "Follow user with id {followingUserId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/follow/{followingUserId}")
    public ResponseObject<EmptyObject> followUser(
            @PathVariable String followingUserId,
            JwtAuthenticationToken token
    ) {
        String followerId = token.getName();
        if (followerId.equals(followingUserId)) throw new CustomValidationException("Cannot follow yourself");

        followService.followUser(followerId, followingUserId);
        return ResponseObject.success(new EmptyObject());
    }

    @Operation(summary = "Unfollow user with id {followingUserId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/unfollow/{followingUserId}")
    public ResponseObject<EmptyObject> unfollowUser(
            @PathVariable String followingUserId,
            JwtAuthenticationToken token
    ) {
        String followerId = token.getName();
        if (followerId.equals(followingUserId)) throw new CustomValidationException("Cannot unfollow yourself");

        followService.unfollowUser(followerId, followingUserId);
        return ResponseObject.success(new EmptyObject());
    }
}
