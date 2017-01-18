package com.indiepost.service.mapper;

import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.response.AdminPostResponseDto;
import com.indiepost.dto.response.AdminPostTableDto;
import com.indiepost.model.Post;

/**
 * Created by jake on 16. 12. 14.
 */
public interface PostMapperService {

    Post postToPost(Post srcPost);

    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);

    void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post);

    AdminPostRequestDto postToAdminPostRequestDto(Post post);

    AdminPostTableDto postToAdminPostTableDto(Post post);
}
