package com.thai.post_service.dto.request;

import com.thai.post_service.model.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {
    private String content;
    private String mediaUrl;
    private Post.PostType postType;
    private Post.Visibility visibility;
}
