package com.indiepost.service;

import com.indiepost.dto.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    PostDto findById(Long id);

    PostDto findByLegacyId(Long id);

    Long count();

    Long count(PostQuery query);

    List<PostSummary> findByIds(List<Long> ids);

    List<PostSummary> findAll(int page, int maxResults, boolean isDesc);

    List<PostSummary> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc);

    List<PostSummary> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc);

    List<PostSummary> findByTagName(String tagName);

    List<RelatedPostResponseDto> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile);

    List<PostSummary> getTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostSummary> getScheduledPosts();

    List<PostSummary> fullTextSearch(FullTextSearchQuery query);

    Long findIdByLegacyId(Long legacyId);

    PostSummary findSplashPost();

    PostSummary findFeaturePost();

    List<PostSummary> findPickedPosts();
}