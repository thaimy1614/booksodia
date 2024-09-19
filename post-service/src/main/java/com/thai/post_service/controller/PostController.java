package com.thai.post_service.controller;

import com.thai.post_service.dto.ResponseObject;
import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.model.Post;
import com.thai.post_service.service.PostService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("${application.api.prefix}")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping()
    public ResponseObject<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "content") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PostResponse> postResponses = postService.getPosts(pageable);
        return ResponseObject.success(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseObject<PostResponse> getPostById(
            @PathVariable String id
    ) {
        PostResponse response = postService.getPost(id);
        return ResponseObject.success(response);
    }

    @PostMapping()
    public ResponseObject<PostResponse> createPost(
            @RequestPart PostRequest postRequest,
            @Nullable @RequestParam("file") MultipartFile multipartFile
    ) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        if (multipartFile != null) {
            String contentType = multipartFile.getContentType();
            if(contentType!=null) {
                switch (contentType) {
                    case "image/jpeg":
                    case "image/png":
                    case "image/gif":
                    case "image/bmp":
                        postRequest.setPostType(Post.PostType.IMAGE);
                        break;
                    default:
                        postRequest.setPostType(Post.PostType.VIDEO);
                }
            }else{
                postRequest.setPostType(Post.PostType.TEXT);
            }
        } else {
            postRequest.setPostType(Post.PostType.TEXT);
        }
        // Create the post
        PostResponse response = postService.createPost(id, postRequest, multipartFile);
        return ResponseObject.success(response);
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
