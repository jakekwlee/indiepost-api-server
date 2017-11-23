package com.indiepost.service;

import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    PostDto findOne(Long id);

    PostDto findOneByLegacyId(Long id);

    Long count();

    Long count(PostSearch search);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByTagName(String tagName, int page, int maxResults, boolean isDesc);

    List<RelatedPostResponse> findRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile);

    List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> search(String text, int page, int maxResults);

    List<PostSummaryDto> search(PostSearch query, int page, int maxResults, boolean isDesc);

    Long findIdByLegacyId(Long legacyId);

    List<PostSummaryDto> findPickedPosts();

    PostSummaryDto findSplashPost();

    PostSummaryDto findFeaturePost();
}