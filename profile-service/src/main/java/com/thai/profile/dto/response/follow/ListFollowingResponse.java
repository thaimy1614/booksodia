package com.thai.profile.dto.response.follow;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListFollowingResponse {
    private List<FollowingResponse> followings;
}
