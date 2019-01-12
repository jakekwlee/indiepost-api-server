package com.indiepost.repository.utils

import com.indiepost.dto.post.PostQuery
import com.indiepost.model.QPost
import com.querydsl.core.BooleanBuilder

/**
 * Created by jake on 17. 1. 14.
 */
object CriteriaUtils {
    fun addConjunction(query: PostQuery, builder: BooleanBuilder): BooleanBuilder {
        val post = QPost.post

        builder.and(post.status.eq(query.status))

        val tagName = query.primaryTag
        if (tagName != null) {
            builder.and(post.primaryTag.name.eq(tagName.toLowerCase()))
        }
        if (query.publishedAfter != null) {
            builder.and(post.publishedAt.goe(query.publishedAfter))
        }
        if (query.publishedBefore != null) {
            builder.and(post.publishedAt.loe(query.publishedBefore))
        }
        if (query.modifiedAfter != null) {
            builder.and(post.modifiedAt.goe(query.modifiedAfter))
        }
        if (query.modifiedBefore != null) {
            builder.and(post.modifiedAt.loe(query.modifiedBefore))
        }
        if (query.splash != null) {
            builder.and(post.isSplash.eq(query.splash!!))
        }
        if (query.featured != null) {
            builder.and(post.isFeatured.eq(query.featured!!))
        }
        if (query.picked != null) {
            builder.and(post.isPicked.eq(query.picked!!))
        }
        return builder
    }

}
