package com.indiepost.mapper;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Contributor;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by jake on 16. 12. 15.
 */
public class PostMapper {

    // copy except id, tags, profiles
    public static Post postToPost(Post srcPost) {
        Post destPost = new Post();
        destPost.setTitle(srcPost.getTitle());
        destPost.setExcerpt(srcPost.getExcerpt());
        destPost.setContent(srcPost.getContent());
        destPost.setDisplayName(srcPost.getDisplayName());
        destPost.setStatus(srcPost.getStatus());
        destPost.setCreator(srcPost.getCreator());
        destPost.setModifiedUser(srcPost.getModifiedUser());
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

    public static PostSummaryDto postToPostSummaryDto(Post post) {
        PostSummaryDto postSummaryDto = new PostSummaryDto();
        postSummaryDto.setId(post.getId());
        postSummaryDto.setFeatured(post.isFeatured());
        postSummaryDto.setSplash(post.isSplash());
        postSummaryDto.setPicked(post.isPicked());
        postSummaryDto.setTitle(post.getTitle());
        postSummaryDto.setExcerpt(post.getExcerpt());
        postSummaryDto.setCategoryId(post.getCategoryId());
        postSummaryDto.setBookmarkCount(post.getBookmarkCount());
        postSummaryDto.setLegacyPostId(post.getLegacyPostId());
        postSummaryDto.setDisplayName(post.getDisplayName());
        postSummaryDto.setTitleImageId(post.getTitleImageId());
        return postSummaryDto;
    }

    public static void adminPostRequestDtoToPost(AdminPostRequestDto requestDto, Post post) {
        if (requestDto.getTitle() != null) {
            post.setTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            post.setContent(requestDto.getContent());
        }
        if (requestDto.getExcerpt() != null && requestDto.getExcerpt().length() > 0) {
            post.setExcerpt(requestDto.getExcerpt());
        }
        if (requestDto.getPublishedAt() != null) {
            post.setPublishedAt(requestDto.getPublishedAt());
        }
        if (requestDto.getDisplayName() != null && requestDto.getDisplayName().length() > 0) {
            post.setDisplayName(requestDto.getDisplayName());
        }
        if (requestDto.getCategoryId() != null) {
            post.setCategoryId(requestDto.getCategoryId());
        }
        if (requestDto.getTitleImageId() != null) {
            post.setTitleImageId(requestDto.getTitleImageId());
        }
        if (requestDto.getStatus() != null) {
            post.setStatus(PostStatus.valueOf(requestDto.getStatus()));
        }
        post.setSplash(requestDto.isSplash());
        post.setFeatured(requestDto.isFeatured());
        post.setPicked(requestDto.isPicked());
    }

    public static void addTagsToPost(Post post, List<Tag> tags) {
        post.getPostTags().clear();
        for (Tag tag : tags) {
            post.addTag(tag);
        }
    }

    public static void addContributorsToPost(Post post, List<Contributor> contributors) {
        post.getPostContributors().clear();
        for (Contributor contributor : contributors) {
            post.addContributor(contributor);
        }
    }

    public static PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        BeanUtils.copyProperties(post, postDto);
        return postDto;
    }

    public static Post adminPostRequestDtoToPost(AdminPostRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setPublishedAt(dto.getPublishedAt());
        post.setContent(dto.getContent());
        post.setExcerpt(dto.getExcerpt());
        post.setDisplayName(dto.getDisplayName());

        post.setSplash(dto.isSplash());
        post.setFeatured(dto.isFeatured());
        post.setPicked(dto.isPicked());

        post.setTitleImageId(dto.getTitleImageId());
        post.setCategoryId(dto.getCategoryId());
        return post;
    }
}
