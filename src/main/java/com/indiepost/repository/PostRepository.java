package com.indiepost.repository;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    Post findById(Long id);

    Long count();

    Long count(PostQuery query);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable);

    @SuppressWarnings("JpaQlInspection")
    List<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable);

    List<PostSummaryDto> findByTagName(String tagName, Pageable pageable);

    List<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable);

    List<PostSummaryDto> findByStatus(Types.PostStatus status, Pageable pageable);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> search(PostQuery search, Pageable pageable);

    Types.PostStatus getStatusById(Long postId);

    List<PostSummaryDto> findUserReadByUserId(Long userId, Pageable pageable);

    List<PostSummaryDto> findUserBookmarksByUserId(Long userId, Pageable pageable);
}