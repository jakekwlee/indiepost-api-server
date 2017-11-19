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

    Post findByLegacyId(Long id);

    Long findIdByLegacyId(Long id);

    Long count();

    Long count(PostQuery query);

    List<PostSummaryDto> find(Pageable pageable);

    List<PostSummaryDto> findByQuery(PostQuery query, Pageable pageable);

    List<PostSummaryDto> findByIds(List<Long> ids);

    List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable);

    List<PostSummaryDto> findByStatus(Types.PostStatus status, Pageable pageable);

    List<PostSummaryDto> findScheduledPosts();

    List<PostSummaryDto> search(String text, Pageable pageable);
}