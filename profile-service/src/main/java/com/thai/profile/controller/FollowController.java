package com.thai.profile.controller;

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
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @GetMapping("/following")
    public ResponseObject<ListFollowingResponse> getAllFollowing(
            JwtAuthenticationToken token
    ) {
        Long userId = Long.parseLong(token.getName());
        return ResponseObject.success(followService.getAllFollowing(userId));
    }

    @Operation(summary = "Count number of following")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @GetMapping("/following/count")
    public ResponseObject<Long> countFollowing(
            JwtAuthenticationToken token
    ) {
        Long userId = Long.parseLong(token.getName());
        return ResponseObject.success(followService.countFollowing(userId));
    }

    @Operation(summary = "Check if the current log in user follow {userId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @GetMapping("/following/check")
    public ResponseObject<Boolean> isFollowed(
            @RequestParam Long following,
            @Autowired JwtAuthenticationToken token
    ) {
        long currentUser = Long.parseLong(token.getName());
        final boolean res = followService.isFollowBy(currentUser, following);
        return ResponseObject.success(res);
    }

    @Operation(summary = "Get all follower")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_INSTRUCTOR_PERMS')")
    @GetMapping("/follower")
    public ResponseObject<ListFollowerResponse> getAllFollower(
            JwtAuthenticationToken token
    ) {
        Long userId = Long.parseLong(token.getName());
        return ResponseObject.success(followService.getAllFollower(userId));
    }

    @Operation(summary = "Count number of followers")
    @GetMapping("/{userId}/follower/count")
    public ResponseObject<Long> countFollower(
            @PathVariable Long userId
    ) {
        return ResponseObject.success(followService.countFollower(userId));
    }

    @Operation(summary = "Count number of followers")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_INSTRUCTOR_PERMS')")
    @GetMapping("/follower/count")
    public ResponseObject<Long> countFollower(
            JwtAuthenticationToken token
    ) {
        Long userId = Long.parseLong(token.getName());
        return ResponseObject.success(followService.countFollower(userId));
    }

    @Operation(summary = "Follow user with id {followingUserId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @PostMapping("/follow/{followingUserId}")
    public ResponseObject<EmptyObject> followUser(
            @PathVariable Long followingUserId,
            JwtAuthenticationToken token
    ) {
        Long followerId = Long.parseLong(token.getName());
        if (followerId.equals(followingUserId)) throw new CustomValidationException("Cannot follow yourself");

        followService.followUser(followerId, followingUserId);
        return ResponseObject.success(new EmptyObject());
    }

    @Operation(summary = "Unfollow user with id {followingUserId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('SCP_STUDENT_PERMS')")
    @DeleteMapping("/unfollow/{followingUserId}")
    public ResponseObject<EmptyObject> unfollowUser(
            @PathVariable Long followingUserId,
            JwtAuthenticationToken token
    ) {
        Long followerId = Long.parseLong(token.getName());
        if (followerId.equals(followingUserId)) throw new CustomValidationException("Cannot unfollow yourself");

        followService.unfollowUser(followerId, followingUserId);
        return ResponseObject.success(new EmptyObject());
    }
}
