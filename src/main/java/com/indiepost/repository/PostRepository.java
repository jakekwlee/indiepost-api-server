package com.indiepost.repository;

import com.indiepost.dto.post.PostQuery;
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

    List<Post> find(Pageable pageable);

    List<Post> findByQuery(PostQuery query, Pageable pageable);

    List<Post> findByIds(List<Long> ids);

    List<Post> findByCategoryId(Long categoryId, Pageable pageable);

    List<Post> findByStatus(Types.PostStatus status, Pageable pageable);

    List<Post> findScheduledPosts();

    List<Post> search(String text, Pageable pageable);
}