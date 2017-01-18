package com.indiepost.service.mapper;

import com.indiepost.dto.TagDto;
import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.response.AdminPostResponseDto;
import com.indiepost.dto.response.AdminPostTableDto;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.service.CategoryService;
import com.indiepost.service.ImageService;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jake on 16. 12. 15.
 */
@Service
public class PostMapperServiceImpl implements PostMapperService {

    private final TagMapperService tagMapperService;

    private final CategoryService categoryService;

    private final ImageService imageService;

    @Autowired
    public PostMapperServiceImpl(TagMapperService tagMapperService, CategoryService categoryService, ImageService imageService) {
        this.tagMapperService = tagMapperService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    @Override
    public Post postToPost(Post srcPost) {
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
        destPost.setTitleImage(srcPost.getTitleImage());
        return destPost;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminPostResponseDto postToAdminPostResponseDto(Post post) {
        AdminPostResponseDto responseDto = new AdminPostResponseDto();
        responseDto.setId(post.getId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setExcerpt(post.getExcerpt());
        responseDto.setDisplayName(post.getDisplayName());
        responseDto.setTitleImage(post.getTitleImage());
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
                tagDtoList.add(tagMapperService.tagToTagDto(tag));
            }
            responseDto.setTags(tagDtoList);
        }
        return responseDto;
    }

    @Override
    @Transactional
    public Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto) {
        Post post = new Post();
        post.setTitle(adminPostRequestDto.getTitle());
        post.setPublishedAt(adminPostRequestDto.getPublishedAt());
        post.setContent(adminPostRequestDto.getContent());
        post.setExcerpt(adminPostRequestDto.getExcerpt());
        post.setDisplayName(adminPostRequestDto.getDisplayName());
        if (adminPostRequestDto.getTitleImageId() != null) {
            post.setTitleImage(
                    imageService.findById(adminPostRequestDto.getTitleImageId())
            );
        }
        if (adminPostRequestDto.getCategoryId() != null) {
            post.setCategory(categoryService.findById(
                    adminPostRequestDto.getCategoryId()
            ));
        }
        postRequestTagDtoListToPostTagSet(adminPostRequestDto.getTags(), post);
        return post;
    }

    @Override
    @Transactional(readOnly = true)
    public void adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto, Post post) {
        if (adminPostRequestDto.getTitle() != null && adminPostRequestDto.getTitle().length() > 0) {
            post.setTitle(adminPostRequestDto.getTitle());
        }
        if (adminPostRequestDto.getContent() != null && adminPostRequestDto.getContent().length() > 0) {
            post.setContent(adminPostRequestDto.getContent());
        }
        if (adminPostRequestDto.getExcerpt() != null && adminPostRequestDto.getExcerpt().length() > 0) {
            post.setExcerpt(adminPostRequestDto.getExcerpt());
        }
        if (adminPostRequestDto.getPublishedAt() != null) {
            post.setPublishedAt(adminPostRequestDto.getPublishedAt());
        }
        if (adminPostRequestDto.getDisplayName() != null && adminPostRequestDto.getDisplayName().length() > 0) {
            post.setDisplayName(adminPostRequestDto.getDisplayName());
        }
        if (adminPostRequestDto.getCategoryId() != null) {
            post.setCategory(
                    categoryService.findById(adminPostRequestDto.getCategoryId()
                    ));
        }
        if (adminPostRequestDto.getTitleImageId() != null) {
            post.setTitleImage(
                    imageService.findById(adminPostRequestDto.getTitleImageId())
            );
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
        requestDto.setCategoryId(post.getCategoryId());
        requestDto.setDisplayName(post.getDisplayName());
        requestDto.setTitleImageId(post.getTitleImageId());
        if (post.getOriginal() != null) {
            requestDto.setOriginalId(post.getOriginalId());
        }
        return requestDto;
    }

    @Override
    public AdminPostTableDto postToAdminPostTableDto(Post post) {
        // TODO
        AdminPostTableDto adminPostTableDto = new AdminPostTableDto();

        adminPostTableDto.setId(post.getId());
        adminPostTableDto.setAuthorDisplayName(post.getAuthor().getDisplayName());
        adminPostTableDto.setCategoryName(post.getCategory().getName());
        adminPostTableDto.setTags(this.tagMapperService.tagListToTagStringList(post.getTags()));
        adminPostTableDto.setStatus(post.getStatus().toString());

        adminPostTableDto.setTitle(post.getTitle());
        adminPostTableDto.setDisplayName(post.getDisplayName());
        adminPostTableDto.setCreatedAt(getDateString(post.getCreatedAt()));
        adminPostTableDto.setPublishedAt(getDateString(post.getPublishedAt()));
        adminPostTableDto.setModifiedAt(getDateString(post.getModifiedAt()));
        adminPostTableDto.setCreatedAt(getDateString(post.getCreatedAt()));
        adminPostTableDto.setDisplayName(post.getDisplayName());
        adminPostTableDto.setLikedCount(post.getLikesCount());
        return adminPostTableDto;
    }

    @Transactional
    private void postRequestTagDtoListToPostTagSet(List<TagDto> tagDtos, Post post) {
        post.clearTags();
        if (tagDtos != null) {
            for (TagDto tagDto : tagDtos) {
                Tag tag = tagMapperService.tagDtoToTag(tagDto);
                post.addTag(tag);
            }
        }
    }

    private String getDateString(Date date) {
        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yy/MM/dd HH:mm ", Locale.KOREA);
        return fastDateFormat.format(date);
    }
}
