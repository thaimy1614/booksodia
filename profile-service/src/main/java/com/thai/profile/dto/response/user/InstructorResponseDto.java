package com.thai.profile.dto.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import fsa.cursus.user_service.dto.course.CategoryDto;
import fsa.cursus.user_service.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstructorResponseDto extends UserResponseDto {
    @JsonProperty(value = "nFollower")
    private Long nFollower;

    @JsonProperty("nStudent")
    private Integer nStudent;

    @JsonProperty("nCourse")
    private Integer nCourse;

    @JsonProperty("categoryId")
    private List<CategoryDto> categoryId;

    public InstructorResponseDto() {
        super(Role.INSTRUCTOR_VALUE);
    }
}
