package com.indiepost.repository;

import com.indiepost.dto.post.PostSearch;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    Post findById(Long id);

    Post findByLegacyId(Long id);

    Long findIdByLegacyId(Long id);

    Long count();

    Long count(PostSearch search);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable);

    List<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable);

    List<PostSummaryDto> findByTagName(String tagName, Pageable pageable);

    List<PostSummaryDto> findByStatus(PostStatus status, Pageable pageable);

    List<PostSummaryDto> findScheduledPosts();

    List<Post> search(String text, Pageable pageable);

    List<PostSummaryDto> search(PostSearch search, Pageable pageable);

    PostStatus getStatusById(Long postId);
}