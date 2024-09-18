package com.thai.post_service.service;

import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<PostResponse> getPosts(Pageable pageable);

    PostResponse getPost(String id);

    PostResponse createPost(PostRequest postRequest);

    PostResponse updatePost(String id, PostRequest postRequest);

    PostResponse deletePost(String id);
}
