package com.indiepost.repository;

import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummary;
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

    List<PostSummary> find(Pageable pageable);

    List<PostSummary> findByQuery(PostQuery query, Pageable pageable);

    List<PostSummary> findByIds(List<Long> ids);

    List<PostSummary> findByCategoryId(Long categoryId, Pageable pageable);

    List<PostSummary> findByStatus(Types.PostStatus status, Pageable pageable);

    List<Post> search(String text, Pageable pageable);
}