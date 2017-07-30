package com.indiepost.service.mapper;

import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostSummary;
import com.indiepost.dto.TagDto;
import com.indiepost.dto.admin.AdminPostRequestDto;
import com.indiepost.dto.admin.AdminPostResponseDto;
import com.indiepost.dto.admin.AdminPostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.service.CategoryService;
import com.indiepost.service.ImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
        destPost.setCategory(srcPost.getCategory());
        destPost.setTitleImage(srcPost.getTitleImage());
        destPost.setSplash(srcPost.isSplash());
        destPost.setFeatured(srcPost.isFeatured());
        destPost.setPicked(srcPost.isPicked());
        return destPost;
    }

    @Override
    public PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        BeanUtils.copyProperties(post, postDto);
        return postDto;
    }

    @Override
    public PostSummary postToPostSummaryDto(Post post) {
        PostSummary postSummary = new PostSummary();
        postSummary.setId(post.getId());
        postSummary.setFeatured(post.isFeatured());
        postSummary.setSplash(post.isSplash());
        postSummary.setPicked(post.isPicked());
        postSummary.setTitle(post.getTitle());
        postSummary.setExcerpt(post.getExcerpt());
        postSummary.setCategoryId(post.getCategoryId());
        postSummary.setCommentsCount(post.getCommentsCount());
        postSummary.setLikesCount(post.getLikesCount());
        postSummary.setLegacyPostId(post.getLegacyPostId());
        postSummary.setDisplayName(post.getDisplayName());
        postSummary.setTitleImageId(post.getTitleImageId());
        return postSummary;
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

        responseDto.setPicked(post.isPicked());
        responseDto.setFeatured(post.isFeatured());
        responseDto.setSplash(post.isSplash());

        responseDto.setCommentsCount(post.getCommentsCount());
        responseDto.setLikesCount(post.getLikesCount());

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

        post.setSplash(adminPostRequestDto.isSplash());
        post.setFeatured(adminPostRequestDto.isFeatured());
        post.setPicked(adminPostRequestDto.isPicked());

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
            post.setPublishedAt(adminPostRequestDto.getPublishedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
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
            post.setStatus(PostStatus.valueOf(adminPostRequestDto.getStatus()));
        }

        post.setSplash(adminPostRequestDto.isSplash());
        post.setFeatured(adminPostRequestDto.isFeatured());
        post.setPicked(adminPostRequestDto.isPicked());
    }

    @Override
    public AdminPostSummaryDto postToAdminPostSummaryDto(Post post) {
        AdminPostSummaryDto adminPostSummaryDto = new AdminPostSummaryDto();

        adminPostSummaryDto.setId(post.getId());
        adminPostSummaryDto.setAuthorDisplayName(post.getAuthor().getDisplayName());
        adminPostSummaryDto.setEditorDisplayName(post.getEditor().getDisplayName());
        adminPostSummaryDto.setCategoryName(post.getCategory().getName());
        adminPostSummaryDto.setTags(this.tagMapperService.tagListToTagStringList(post.getTags()));
        adminPostSummaryDto.setStatus(post.getStatus().toString());

        adminPostSummaryDto.setTitle(post.getTitle());
        adminPostSummaryDto.setDisplayName(post.getDisplayName());
        adminPostSummaryDto.setCreatedAt(post.getCreatedAt());
        adminPostSummaryDto.setPublishedAt(post.getPublishedAt());
        adminPostSummaryDto.setModifiedAt(post.getModifiedAt());
        adminPostSummaryDto.setLikedCount(post.getLikesCount());

        adminPostSummaryDto.setSplash(post.isSplash());
        adminPostSummaryDto.setFeatured(post.isFeatured());
        adminPostSummaryDto.setPicked(post.isPicked());
        return adminPostSummaryDto;
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
}
