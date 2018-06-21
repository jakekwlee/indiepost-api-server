package com.indiepost.repository.utils;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.model.QPost;
import com.querydsl.core.BooleanBuilder;

/**
 * Created by jake on 17. 1. 14.
 */
public interface CriteriaUtils {
    static BooleanBuilder addConjunction(PostQuery query, BooleanBuilder builder) {
        QPost post = QPost.post;

        builder.and(post.status.eq(query.getStatus()));

        if (query.getCategory() != null) {
            builder.and(post.category.slug.eq(query.getCategory()));
        }
        if (query.getPublishedAfter() != null) {
            builder.and(post.publishedAt.goe(query.getPublishedAfter()));
        }
        if (query.getPublishedBefore() != null) {
            builder.and(post.publishedAt.loe(query.getPublishedBefore()));
        }
        if (query.getModifiedAfter() != null) {
            builder.and(post.modifiedAt.goe(query.getModifiedAfter()));
        }
        if (query.getModifiedBefore() != null) {
            builder.and(post.modifiedAt.loe(query.getModifiedBefore()));
        }
        if (query.getSplash() != null) {
            builder.and(post.splash.eq(query.getSplash()));
        }
        if (query.getFeatured() != null) {
            builder.and(post.featured.eq(query.getFeatured()));
        }
        if (query.getPicked() != null) {
            builder.and(post.picked.eq(query.getPicked()));
        }
        return builder;
    }
}
