package com.indiepost.service;

import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    PostDto findOne(Long id);

    Long count();

    Long count(PostQuery query);

    Page<PostSummaryDto> find(Pageable pageable);

    Page<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable);

    Page<PostSummaryDto> findByTagName(String tagName, Pageable pageable);

    Page<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable);

    Page<PostSummaryDto> findReadingHistoryByUserId(Long userId, Pageable pageable);

    Page<PostSummaryDto> findBookmarksByUserId(Long userId, Pageable pageable);

    Page<PostSummaryDto> query(PostQuery postQuery, int page, int size);

    Page<PostSummaryDto> fullTextSearch(FullTextSearchQuery query);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Integer limit);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> findPickedPosts();

    PostSummaryDto findSplashPost();

    PostSummaryDto findFeaturePost();
}