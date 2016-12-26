package com.indiepost.mapper;

import com.indiepost.model.Post;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;

/**
 * Created by jake on 16. 12. 14.
 */
public interface PostMapper {

    Post postToPostMapper(Post srcPost);

    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);

    void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post);

    AdminPostRequestDto postToAdminPostRequestDto(Post post);
}
