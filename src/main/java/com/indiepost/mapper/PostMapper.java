package com.indiepost.mapper;

import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Contributor;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.utils.DomUtil.htmlToText;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by jake on 16. 12. 15.
 */
public class PostMapper {

    // copy except id, tags, contributor
    public static Post duplicate(Post srcPost) {
        Post destPost = new Post();
        destPost.setTitle(srcPost.getTitle());
        destPost.setExcerpt(srcPost.getExcerpt());
        destPost.setContent(srcPost.getContent());
        destPost.setBylineName(srcPost.getBylineName());
        destPost.setStatus(srcPost.getStatus());
        destPost.setCreatedAt(srcPost.getCreatedAt());
        destPost.setPublishedAt(srcPost.getPublishedAt());
        destPost.setModifiedAt(srcPost.getModifiedAt());
        destPost.setSplash(srcPost.isSplash());
        destPost.setFeatured(srcPost.isFeatured());
        destPost.setPicked(srcPost.isPicked());
        destPost.setCategoryId(srcPost.getCategoryId());
        destPost.setCreatorId(srcPost.getCreatorId());
        destPost.setModifiedUserId(srcPost.getModifiedUserId());
        destPost.setTitleImageId(srcPost.getTitleImageId());
        return destPost;
    }

    public static PostSummaryDto toSummaryDto(Post post) {
        PostSummaryDto postSummaryDto = new PostSummaryDto();
        postSummaryDto.setId(post.getId());
        postSummaryDto.setFeatured(post.isFeatured());
        postSummaryDto.setSplash(post.isSplash());
        postSummaryDto.setPicked(post.isPicked());
        postSummaryDto.setTitle(post.getTitle());
        postSummaryDto.setExcerpt(post.getExcerpt());
        postSummaryDto.setBookmarkCount(post.getBookmarkCount());
        postSummaryDto.setLegacyPostId(post.getLegacyPostId());
        postSummaryDto.setBylineName(post.getBylineName());
        return postSummaryDto;
    }

    public static void copyDtoToPost(AdminPostRequestDto requestDto, Post post) {
        if (isNotEmpty(requestDto.getTitle())) {
            post.setTitle(requestDto.getTitle());
        }
        if (isNotEmpty(requestDto.getContent())) {
            post.setContent(requestDto.getContent());
        }
        if (isNotEmpty(requestDto.getExcerpt())) {
            post.setExcerpt(requestDto.getExcerpt());
        }
        if (requestDto.getPublishedAt() != null) {
            post.setPublishedAt(requestDto.getPublishedAt());
        }
        if (isNotEmpty(requestDto.getBylineName())) {
            post.setBylineName(requestDto.getBylineName());
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
        if (tags == null) {
            return;
        }
        post.clearTags();
        for (Tag tag : tags) {
            post.addTag(tag);
        }
    }

    public static void addContributorsToPost(Post post, List<Contributor> contributors) {
        if (contributors == null) {
            return;
        }
        post.clearContributors();
        for (Contributor contributor : contributors) {
            post.addContributor(contributor);
        }
    }

    public static PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        BeanUtils.copyProperties(post, postDto);
        return postDto;
    }

    public static Post copyDtoToPost(AdminPostRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setPublishedAt(dto.getPublishedAt());
        post.setContent(dto.getContent());
        post.setExcerpt(dto.getExcerpt());
        post.setBylineName(dto.getBylineName());

        post.setSplash(dto.isSplash());
        post.setFeatured(dto.isFeatured());
        post.setPicked(dto.isPicked());

        post.setTitleImageId(dto.getTitleImageId());
        post.setCategoryId(dto.getCategoryId());
        return post;
    }

    public static PostEs toPostEs(Post post) {
        PostEs postEs = new PostEs();
        postEs.setId(post.getId());
        postEs.setTitle(post.getTitle());
        postEs.setCreatorId(post.getCreatorId());
        postEs.setModifiedUserId(post.getModifiedUserId());
        postEs.setBylineName(post.getBylineName());
        postEs.setExcerpt(post.getExcerpt());
        postEs.setStatus(post.getStatus().toString());
        postEs.setCategoryName(post.getCategory().getName());
        postEs.setModifiedUserName(post.getModifiedUser().getDisplayName());
        postEs.setCreatorName(post.getCreator().getDisplayName());

        List<String> contributors = post.getContributors().stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());
        postEs.setContributors(contributors);

        List<String> tags = post.getTags().stream()
                .map(t -> t.getName().replaceAll("_", " "))
                .collect(Collectors.toList());
        postEs.setTags(tags);
        postEs.setContent(htmlToText(post.getContent()));
        return postEs;
    }

    public static ImageSetDto imageSetToDto(ImageSet imageSet) {
        ImageSetDto imageSetDto = new ImageSetDto();
        imageSetDto.setId(imageSet.getId());
        if (imageSet.getOriginal() != null) {
            imageSetDto.setOriginal(imageSet.getOriginal().getFilePath());
            imageSetDto.setWidth(imageSet.getOriginal().getWidth());
            imageSetDto.setHeight(imageSet.getOriginal().getHeight());
        } else {
            imageSetDto.setWidth(700);
            imageSetDto.setHeight(400);
        }
        if (imageSet.getLarge() != null) {
            imageSetDto.setLarge(imageSet.getLarge().getFilePath());
        }
        if (imageSet.getOptimized() != null) {
            imageSetDto.setOptimized(imageSet.getOptimized().getFilePath());
        }
        if (imageSet.getSmall() != null) {
            imageSetDto.setSmall(imageSet.getSmall().getFilePath());
        }
        if (imageSet.getThumbnail() != null) {
            imageSetDto.setThumbnail(imageSet.getThumbnail().getFilePath());
        }
        return imageSetDto;
    }
}
