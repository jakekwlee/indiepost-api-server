package com.indiepost.service;

import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
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

    Long count(PostQuery query);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByQuery(PostQuery query, boolean isDesc);

    List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByTagName(String tagName);

    List<RelatedPostResponse> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile);

    List<PostSummaryDto> getTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostSummaryDto> getScheduledPosts();

    List<PostSummaryDto> search(String text, int page, int maxResults);

    Long findIdByLegacyId(Long legacyId);

    PostSummaryDto findSplashPost();

    PostSummaryDto findFeaturePost();

    List<PostSummaryDto> findPickedPosts();
}