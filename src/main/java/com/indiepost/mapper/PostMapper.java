package com.indiepost.mapper;

import com.indiepost.dto.response.AdminPostTableDto;
import com.indiepost.model.Post;
import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.response.AdminPostResponseDto;

/**
 * Created by jake on 16. 12. 14.
 */
public interface PostMapper {

    Post postToPost(Post srcPost);

    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);

    void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post);

    AdminPostRequestDto postToAdminPostRequestDto(Post post);

    AdminPostTableDto postToAdminPostTableDto(Post post);
}
