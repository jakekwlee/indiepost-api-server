package com.indiepost.mapper;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.service.CategoryService;
import dto.TagDto;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 16. 12. 15.
 */
@Component
public class PostMapperImpl implements PostMapper {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    CategoryService categoryService;

    @Override
    public Post postToPostMapper(Post srcPost) {
        Post destPost = new Post();
        destPost.setTitle(srcPost.getTitle());
        destPost.setExcerpt(srcPost.getExcerpt());
        destPost.setContent(srcPost.getContent());
        destPost.setDisplayName(srcPost.getDisplayName());
        destPost.setStatus(srcPost.getStatus());
        destPost.setAuthor(srcPost.getAuthor());
        destPost.setEditor(srcPost.getEditor());
        destPost.setCreatedAt(srcPost.getCreatedAt());
        destPost.setPublishedAt(srcPost.getPublishedAt());
        destPost.setModifiedAt(srcPost.getModifiedAt());
        destPost.setPostType(srcPost.getPostType());
        destPost.setCategory(srcPost.getCategory());
        destPost.setFeaturedImage(srcPost.getFeaturedImage());
        return destPost;
    }

    @Override
    public AdminPostResponseDto postToAdminPostResponseDto(Post post) {
        AdminPostResponseDto responseDto = new AdminPostResponseDto();
        responseDto.setId(post.getId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setExcerpt(post.getExcerpt());
        responseDto.setDisplayName(post.getDisplayName());
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

        responseDto.setPostType(post.getPostType().toString());

        if (post.getOriginal() != null) {
            responseDto.setOriginalId(post.getOriginal().getId());
        }
        if (post.getTags() != null) {
            List<TagDto> tagDtoList = new ArrayList<>();
            for (Tag tag : post.getTags()) {
                tagDtoList.add(tagMapper.tagToTagDto(tag));
            }
            responseDto.setTags(tagDtoList);
        }
        return responseDto;
    }

    @Override
    public Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto) {
        Post post = new Post();
        post.setTitle(adminPostRequestDto.getTitle());
        post.setPublishedAt(adminPostRequestDto.getPublishedAt());
        post.setContent(adminPostRequestDto.getContent());
        post.setExcerpt(adminPostRequestDto.getExcerpt());
        post.setDisplayName(adminPostRequestDto.getDisplayName());
        post.setFeaturedImage(adminPostRequestDto.getFeaturedImage());
        post.setCategory(categoryService.findById(
                adminPostRequestDto.getCategoryId()
        ));
        postRequestTagDtoListToPostTagSet(adminPostRequestDto.getTags(), post);

        return post;
    }

    @Override
    public void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post) {
        if (adminPostRequestDto.getTitle() != null || adminPostRequestDto.getTitle().length() > 0) {
            post.setTitle(adminPostRequestDto.getTitle());
        }
        if (adminPostRequestDto.getContent() != null || adminPostRequestDto.getContent().length() > 0) {
            post.setContent(adminPostRequestDto.getContent());
        }
        if (adminPostRequestDto.getExcerpt() != null || adminPostRequestDto.getExcerpt().length() > 0) {
            post.setExcerpt(adminPostRequestDto.getExcerpt());
        }
        if (adminPostRequestDto.getPublishedAt() != null) {
            post.setPublishedAt(adminPostRequestDto.getPublishedAt());
        }
        if (adminPostRequestDto.getDisplayName() != null || adminPostRequestDto.getDisplayName().length() > 0) {
            post.setDisplayName(adminPostRequestDto.getDisplayName());
        }
        if (adminPostRequestDto.getCategoryId() != null) {
            post.setCategory(categoryService.findById(
                    adminPostRequestDto.getCategoryId()
            ));
        }
        if (adminPostRequestDto.getFeaturedImage() != null) {
            post.setFeaturedImage(adminPostRequestDto.getFeaturedImage());
        }
        if (adminPostRequestDto.getTags() != null) {
            postRequestTagDtoListToPostTagSet(adminPostRequestDto.getTags(), post);
        }
        if (adminPostRequestDto.getStatus() != null) {
            post.setStatus(PostEnum.Status.valueOf(adminPostRequestDto.getStatus()));
        }
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

    private void postRequestTagDtoListToPostTagSet(List<TagDto> tagDtos, Post post) {
        post.clearTags();
        if (tagDtos != null) {
            for (TagDto tagDto : tagDtos) {
                Tag tag = tagMapper.tagDtoToTag(tagDto);
                post.addTag(tag);
            }
        }
    }
}
