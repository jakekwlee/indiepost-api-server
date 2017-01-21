package com.indiepost.service.mapper;

import com.indiepost.dto.AdminPostRequestDto;
import com.indiepost.dto.AdminPostResponseDto;
import com.indiepost.dto.AdminPostSummaryDto;
import com.indiepost.dto.PostDto;
import com.indiepost.model.Post;

/**
 * Created by jake on 16. 12. 14.
 */
public interface PostMapperService {

    Post postToPost(Post srcPost);

    PostDto postToPostDto(Post post);

    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);

    void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post);

    AdminPostSummaryDto postToAdminPostSummaryDto(Post post);
}
