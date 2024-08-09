package com.thai.profile.dto.response.follow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerResponse {
    private String id;
    private String name;
    private String image;
    private String title;
    private long nFollower;
    private long nCourse;
}
