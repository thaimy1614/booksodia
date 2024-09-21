package com.thai.post_service.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.exception.white.PostNotFoundException;
import com.thai.post_service.mapper.PostMapper;
import com.thai.post_service.model.Post;
import com.thai.post_service.repository.PostRepository;
import com.thai.post_service.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AmazonS3 s3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public Page<PostResponse> getPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    public PostResponse getPost(String id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse createPost(String id, PostRequest postRequest, MultipartFile file) {
        Post post = postMapper.toPost(postRequest);
        try {
            post.setUserId(id);
            post = postRepository.save(post);
            if (file != null) {
                upload(file, post.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return postMapper.toPostResponse(post);
    }

    @Override
    public PostResponse updatePost(String id, PostRequest postRequest, MultipartFile file) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        if (existingPost.getMediaUrl() != null && !existingPost.getMediaUrl().isEmpty()) {
            deleteMedia(id);
            existingPost.setMediaUrl(null);
        }
        if (file != null) {
            upload(file, id);
            existingPost.setPostType(getTypeOfMedia(file));
        } else {
            existingPost.setPostType(Post.PostType.TEXT);
        }
        existingPost.setContent(postRequest.getContent());
        existingPost.setVisibility(postRequest.getVisibility());
        Post updatedPost = postRepository.save(existingPost);

        return postMapper.toPostResponse(updatedPost);
    }

    private Post.PostType getTypeOfMedia(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            return switch (contentType) {
                case "image/jpeg", "image/png", "image/gif", "image/bmp" -> Post.PostType.IMAGE;
                default -> Post.PostType.VIDEO;
            };
        } else {
            return Post.PostType.TEXT;
        }
    }

    @Override
    public void deletePost(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        // Delete the post from the repository
        postRepository.delete(post);
        deleteMedia(id);
    }

    @Override
    public Page<PostResponse> getPostsOfUser(Pageable pageable) {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Post> posts = postRepository.findAllByUserId(myId, pageable);
        return posts.map(postMapper::toPostResponse);
    }

    @Override
    public Page<PostResponse> getPostsOfUser(String userId, Pageable pageable) {
        // Fetch public posts
        Page<Post> publicPosts = postRepository.findAllByUserIdAndVisibility(userId, Post.Visibility.PUBLIC, pageable);

        if (SecurityContextHolder.getContext() != null) {
            String myId = SecurityContextHolder.getContext().getAuthentication().getName();

            // Check if either user has blocked the other
            boolean isBlocked = relationshipService.isBlocked(myId, userId);
            if (isBlocked) {
                return null;  // Return null if either user has blocked the other
            }

            // Check if they are friends
            boolean isFriend = relationshipService.areFriends(myId, userId);


            if (isFriend) {
                // Fetch "friend only" posts
                Page<Post> friendOnlyPosts = postRepository.findAllByUserIdAndVisibility(userId, Post.Visibility.FRIENDS_ONLY, pageable);

                // Combine public and friend-only posts
                List<Post> combinedPosts = new ArrayList<>();
                combinedPosts.addAll(publicPosts.getContent());
                combinedPosts.addAll(friendOnlyPosts.getContent());

                return new PageImpl<>(combinedPosts, pageable, combinedPosts.size()).map(postMapper::toPostResponse);
            }
        }

        return publicPosts.map(postMapper::toPostResponse);
    }


    @Async
    public void upload(MultipartFile file, String id) {
        try {
            log.info("Uploading image to {}", id);
            InputStream in = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(in.available());
            PutObjectRequest request = new PutObjectRequest(bucketName, id, file.getInputStream(), metadata);
            s3.putObject(request);
            String url = getUrl(id);
            log.info(url);
            Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
            post.setMediaUrl(url);
            postRepository.save(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl(String objectName) {
        return s3.getUrl(bucketName, objectName).toString();
    }

    private void deleteMedia(String id) {
        s3.deleteObject(bucketName, id);
    }
}
