package com.indiepost.service.mapper;

import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.dto.admin.AdminPostRequestDto;
import com.indiepost.dto.admin.AdminPostResponseDto;
import com.indiepost.dto.admin.AdminPostSummaryDto;
import com.indiepost.model.Post;

/**
 * Created by jake on 16. 12. 14.
 */
public interface PostMapperService {

    Post postToPost(Post srcPost);

    PostDto postToPostDto(Post post);

    PostSummaryDto postToPostSummaryDto(Post post);

    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);

    void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post);

    AdminPostSummaryDto postToAdminPostSummaryDto(Post post);
}
