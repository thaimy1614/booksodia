package com.begin.bg.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileCreationRequest {
    private String userId;
    private String email;
    private String fullName;
    private Boolean isAdmin;
    private String image;
}
