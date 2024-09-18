package com.thai.post_service.controller;

import com.thai.post_service.dto.ResponseObject;
import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${application.api.prefix}")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("")
    public ResponseObject<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> postResponses = postService.getPosts(pageable);
        return ResponseObject.success(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseObject<PostResponse> getPostById(
            @PathVariable String id
            ) {
        return null;
    }

    @PostMapping("")
    public ResponseObject<PostResponse> createPost(
            @RequestBody PostRequest postRequest) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseObject<PostResponse> updatePost(
            @PathVariable String id,
            @RequestBody PostRequest postRequest) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deletePostById(
            @PathVariable String id) {

    }

}
