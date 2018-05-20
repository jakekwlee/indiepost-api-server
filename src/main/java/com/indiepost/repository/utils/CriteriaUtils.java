package com.indiepost.repository.utils;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.model.QPost;
import com.querydsl.core.BooleanBuilder;

import java.time.LocalDateTime;

import static com.indiepost.utils.DateUtil.instantToLocalDateTime;

/**
 * Created by jake on 17. 1. 14.
 */
public interface CriteriaUtils {
    static BooleanBuilder addSearchConjunction(PostQuery search, BooleanBuilder builder) {
        QPost post = QPost.post;
        if (search.getStatus() != null) {
            builder.and(post.status.eq(search.getStatus()));
        }
        if (search.getAuthorId() != null) {
            builder.and(post.authorId.eq(search.getAuthorId()));
        }
        if (search.getEditorId() != null) {
            builder.and(post.editorId.eq(search.getEditorId()));
        }
        if (search.getCategoryId() != null) {
            builder.and(post.categoryId.eq(search.getCategoryId()));
        }
        if (search.getCategorySlug() != null) {
            builder.and(post.category.slug.eq(search.getCategorySlug()));
        }
        if (search.getPublishedAfter() != null) {
            LocalDateTime publishedAfter = instantToLocalDateTime(search.getPublishedAfter());
            builder.and(post.publishedAt.goe(publishedAfter));
        }
        if (search.getPublishedBefore() != null) {
            LocalDateTime publishedBefore = instantToLocalDateTime(search.getPublishedBefore());
            builder.and(post.publishedAt.loe(publishedBefore));
        }
        if (search.getCreatedAfter() != null) {
            LocalDateTime createdAfter = instantToLocalDateTime(search.getCreatedAfter());
            builder.and(post.createdAt.goe(createdAfter));
        }
        if (search.getCreatedBefore() != null) {
            LocalDateTime createdBefore = instantToLocalDateTime(search.getCreatedBefore());
            builder.and(post.createdAt.loe(createdBefore));
        }
        if (search.getModifiedAfter() != null) {
            LocalDateTime modifiedAfter = instantToLocalDateTime(search.getModifiedAfter());
            builder.and(post.modifiedAt.goe(modifiedAfter));
        }
        if (search.getModifiedBefore() != null) {
            LocalDateTime modifiedBefore = instantToLocalDateTime(search.getModifiedBefore());
            builder.and(post.modifiedAt.loe(modifiedBefore));
        }
        builder.and(post.splash.eq(search.isSplash()));
        builder.and(post.featured.eq(search.isFeatured()));
        builder.and(post.picked.eq(search.isPicked()));
        return builder;
    }
}
