package com.thai.post_service.service.impl;

import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.mapper.PostMapper;
import com.thai.post_service.model.Post;
import com.thai.post_service.repository.PostRepository;
import com.thai.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    public Page<PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    public PostResponse getPost(String id) {
        return null;
    }

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        return null;
    }

    @Override
    public PostResponse updatePost(String id, PostRequest postRequest) {
        return null;
    }

    @Override
    public PostResponse deletePost(String id) {
        return null;
    }
}
