package com.thai.post_service.service;

import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    Page<PostResponse> getPosts(Pageable pageable);

    PostResponse getPost(String id);

    PostResponse createPost(String id, PostRequest postRequest, MultipartFile file);

    PostResponse updatePost(String id, PostRequest postRequest, MultipartFile file);

    void deletePost(String id);

    Page<PostResponse> getPostsOfUser(Pageable pageable);

    Page<PostResponse> getPostsOfUser(String userId, Pageable pageable);
}
