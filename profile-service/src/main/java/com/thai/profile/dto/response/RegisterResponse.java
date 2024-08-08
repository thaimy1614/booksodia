package com.thai.profile.dto.response;

import fsa.cursus.user_service.dto.course.CategoryDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisterResponse {
    private String userId;
    private String instructorName;
    private String description;
    private List<CategoryDto> categoryId;
    private String cv;
}
