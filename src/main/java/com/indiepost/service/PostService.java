package com.indiepost.service;

import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.dto.RelatedPostResponseDto;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    PostDto findById(Long id);

    PostDto findByLegacyId(Long id);

    Long count();

    Long count(PostQuery query);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findAll(int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc);

    List<PostSummaryDto> findByTagName(String tagName);

    List<RelatedPostResponseDto> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile);

    List<PostSummaryDto> search(String text, int page, int maxResults);
}