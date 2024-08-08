package com.thai.profile.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowId implements Serializable {
    private User follower;
    private User following;
}
