package com.indiepost.service;

import com.indiepost.dto.post.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    PostDto findOne(Long id);

    PostDto findOneByLegacyId(Long id);

    Long count();

    Long count(PostQuery search);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByTagName(String tagName, int page, int maxResults, boolean isDesc);

    List<RelatedPostResponse> findRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile);

    List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> search(PostQuery query, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> fullTextSearch(FullTextSearchQuery query);

    Long findIdByLegacyId(Long legacyId);

    List<PostSummaryDto> findPickedPosts();

    PostSummaryDto findSplashPost();

    PostSummaryDto findFeaturePost();

}