package com.indiepost.mapper;

import com.indiepost.model.Post;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jake on 16. 12. 15.
 */
@Component
public class PostMapperImpl implements PostMapper {

    @Autowired
    TagMapper tagMapper;

    @Override
    public AdminPostResponseDto postToAdminPostResponseDto(Post post) {
        AdminPostResponseDto responseDto = new AdminPostResponseDto();
        responseDto.setId(post.getId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setExcerpt(post.getExcerpt());
        responseDto.setFeaturedImage(post.getFeaturedImage());
        responseDto.setStatus(post.getStatus().toString());

        responseDto.setCreatedAt(post.getCreatedAt());
        responseDto.setModifiedAt(post.getModifiedAt());
        responseDto.setPublishedAt(post.getPublishedAt());

        responseDto.setCategoryId(post.getCategory().getId());
        responseDto.setAuthorId(post.getAuthor().getId());
        responseDto.setEditorId(post.getEditor().getId());

        responseDto.setCommentsCount(post.getCommentsCount());
        responseDto.setLikesCount(post.getLikesCount());

        responseDto.setPostType(post.getStatus().toString());

        if (post.getOriginal() != null) {
            responseDto.setOriginalId(post.getOriginal().getId());
        }
        return responseDto;
    }

    @Override
    public Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto) {
        Post post = new Post();
        post.setId(adminPostRequestDto.getId());
        post.setTitle(adminPostRequestDto.getTitle());
        return null;
    }

    @Override
    public AdminPostRequestDto postToAdminPostRequestDto(Post post) {
        AdminPostRequestDto requestDto = new AdminPostRequestDto();
        requestDto.setId(post.getId());
        requestDto.setTitle(post.getTitle());
        requestDto.setContent(post.getContent());
        requestDto.setExcerpt(post.getExcerpt());
        requestDto.setStatus(post.getStatus().toString());
        requestDto.setCategoryId(post.getCategory().getId());
        requestDto.setDisplayName(post.getDisplayName());
        requestDto.setFeaturedImage(post.getFeaturedImage());
        if (post.getOriginal() != null) {
            requestDto.setOriginalId(post.getOriginal().getId());
        }
        return requestDto;
    }
}
