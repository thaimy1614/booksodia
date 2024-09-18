package com.thai.post_service.controller;

import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.api.prefix}")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public Page<PostResponse> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ;
    }

    @GetMapping("/{id}")
    public Page<PostResponse> getPostById(
            @PathVariable String id
            ) {
        return null;
    }

    @PostMapping("")
    public Page<PostResponse> createPost(
            @RequestBody PostRequest postRequest) {
        return null;
    }

    @PutMapping("/{id}")
    public Page<PostResponse> updatePost(
            @PathVariable String id,
            @RequestBody PostRequest postRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public Page<PostResponse> deletePostById(
            @PathVariable String id) {
        return null;
    }

}
