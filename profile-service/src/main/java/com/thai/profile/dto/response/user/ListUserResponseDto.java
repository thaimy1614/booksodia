package com.thai.profile.dto.response.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderClassName = "Builder")
public class ListUserResponseDto {
    private Integer nextPage;
    private Integer nextSize;
    private Integer totalPage;
    private Integer totalElement;
    private List<UserResponseDto> users;
}
