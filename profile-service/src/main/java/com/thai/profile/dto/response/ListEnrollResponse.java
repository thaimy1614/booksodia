package com.thai.profile.dto.response;

import fsa.cursus.user_service.dto.course.CourseInternal;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListEnrollResponse {
    List<CourseInternal> courses;
}
