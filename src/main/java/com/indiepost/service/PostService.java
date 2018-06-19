package com.indiepost.service;

import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
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

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> find(Pageable pageable);

    List<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable);

    List<PostSummaryDto> findByTagName(String tagName, Pageable pageable);

    List<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable);

    List<PostSummaryDto> findUserReadByUserId(Long userId, Pageable pageable);

    List<PostSummaryDto> findUserBookmarksByUserId(Long userId, Pageable pageable);

    List<PostSummaryDto> search(PostQuery query, int page, int size);

    List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Integer limit);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> fullTextSearch(FullTextSearchQuery query);

    PostSummaryDto findSplashPost();

    PostSummaryDto findFeaturePost();

    List<PostSummaryDto> findPickedPosts();
}