package com.thai.post_service.mapper;

import com.thai.post_service.dto.request.PostRequest;
import com.thai.post_service.dto.response.PostResponse;
import com.thai.post_service.model.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponse toPostResponse(Post post);
    Post toPost(PostRequest postRequest);
}
