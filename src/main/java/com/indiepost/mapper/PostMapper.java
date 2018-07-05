package com.indiepost.mapper;

import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostUserInteraction;
import com.indiepost.dto.post.RelatedPostsMatchingResult;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.utils.DateUtil.instantToLocalDateTime;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;
import static com.indiepost.utils.DomUtil.getRelatedPostIdsFromPostContent;
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
        destPost.setDisplayName(srcPost.getDisplayName());
        destPost.setStatus(srcPost.getStatus());
        destPost.setCreatedAt(srcPost.getCreatedAt());
        destPost.setPublishedAt(srcPost.getPublishedAt());
        destPost.setModifiedAt(srcPost.getModifiedAt());
        destPost.setSplash(srcPost.isSplash());
        destPost.setFeatured(srcPost.isFeatured());
        destPost.setPicked(srcPost.isPicked());
        return destPost;
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
            post.setPublishedAt(instantToLocalDateTime(requestDto.getPublishedAt()));
        }
        if (isNotEmpty(requestDto.getDisplayName())) {
            post.setDisplayName(requestDto.getDisplayName());
        }
        if (requestDto.getStatus() != null) {
            post.setStatus(PostStatus.valueOf(requestDto.getStatus().toUpperCase()));
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
        int priority = 0;
        for (Tag tag : tags) {
            post.addTag(tag, priority++);
        }
    }

    public static void addContributorsToPost(Post post, List<Contributor> contributors) {
        if (contributors == null) {
            return;
        }
        post.clearContributors();
        int i = 0;
        for (Contributor contributor : contributors) {
            post.addContributor(contributor, i++);
        }
    }

    public static PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        BeanUtils.copyProperties(post, postDto);
        postDto.setPublishedAt(localDateTimeToInstant(post.getPublishedAt()));
        postDto.setCategoryName(post.getCategory().getName());
        RelatedPostsMatchingResult result = getRelatedPostIdsFromPostContent(post.getContent());
        if (result != null && result.getIds().size() > 0) {
            postDto.setRelatedPostIds(result.getIds());
            postDto.setContent(result.getContent());
        }
        return postDto;
    }

    public static Post copyDtoToPost(AdminPostRequestDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        // TODO 20180508
        post.setPublishedAt(instantToLocalDateTime(dto.getPublishedAt()));
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

    public static PostEs toPostEs(Post post) {
        PostEs postEs = new PostEs();
        postEs.setId(post.getId());
        postEs.setTitle(post.getTitle());
        postEs.setCreatorId(post.getAuthorId());
        postEs.setModifiedUserId(post.getEditorId());
        postEs.setBylineName(post.getDisplayName());
        postEs.setExcerpt(post.getExcerpt());
        postEs.setStatus(post.getStatus().toString());
        postEs.setCategoryName(post.getCategory().getName());
        postEs.setModifiedUserName(post.getEditor().getDisplayName());
        postEs.setCreatorName(post.getAuthor().getDisplayName());

        List<String> contributors = post.getContributors().stream()
                .map(c -> c.getFullName())
                .collect(Collectors.toList());
        postEs.setContributors(contributors);

        List<String> tags = post.getTags().stream()
                .map(t -> t.getName().replaceAll("_", " "))
                .collect(Collectors.toList());
        postEs.setTags(tags);
        postEs.setContent(htmlToText(post.getContent()));
        return postEs;
    }

    public static PostUserInteraction toPostInteractionDto(PostReading postReading) {
        PostUserInteraction dto = new PostUserInteraction(postReading.getPostId());
        dto.setLastRead(localDateTimeToInstant(postReading.getLastRead()));
        return dto;
    }

    public static ImageSetDto imageSetToDto(ImageSet imageSet) {
        ImageSetDto imageSetDto = new ImageSetDto();
        imageSetDto.setId(imageSet.getId());
        imageSetDto.setContentType(imageSet.getContentType());
        imageSetDto.setUploadedAt(localDateTimeToInstant(imageSet.getUploadedAt()));
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
