package com.indiepost.repository;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 1. 10.
 */
public interface AdminPostRepository {

    Long save(Post post);

    Post findById(Long id);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);

    List<Post> find(User user, Pageable pageable);

    List<Post> find(User user, PostQuery postQuery, Pageable pageable);

    List<String> findAllDisplayNames();

    Long count();

    Long count(PostQuery postQuery);

    List<Post> findScheduledToBePublished();

    List<Post> findScheduledToBeIndexed(LocalDateTime indicesLastUpdatedAt);

    void disableSplashPosts();

    void disableFeaturedPosts();
}
