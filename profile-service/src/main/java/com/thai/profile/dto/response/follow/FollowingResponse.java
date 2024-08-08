package com.thai.profile.dto.response.follow;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowingResponse {
    private long id;
    private String name;
    private String image;
    private String title;
    @Builder.Default
    private long nFollower = 0L;
    @Builder.Default
    private long nCourse = 0L;
}
