package com.indiepost.repository;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    Post findById(Long id);

    Long count();

    Long count(PostQuery query);

    Types.PostStatus getStatusById(Long postId);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findScheduledPosts();

    Page<PostSummaryDto> query(PostQuery postQuery, Pageable pageable);

    Page<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable);

    Page<PostSummaryDto> findByTagName(String tagName, Pageable pageable);

    Page<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable);

    Page<PostSummaryDto> findByStatus(Types.PostStatus status, Pageable pageable);

    Page<PostSummaryDto> findReadingHistoryByUserId(Long userId, Pageable pageable);

    Page<PostSummaryDto> findBookmarksByUserId(Long userId, Pageable pageable);
}