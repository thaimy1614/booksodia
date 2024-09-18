package com.thai.post_service.dto.response;

import com.thai.post_service.model.Post;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private String id;
    private String content;
    private String mediaUrl;
    private Post.PostType postType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Post.Visibility visibility;
}
