package com.indiepost.repository;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostSearch;
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

    Post findOne(Long id);

    Post merge(Post post);

    void delete(Post post);

    void deleteById(Long id);

    List<AdminPostSummaryDto> find(User currentUser, Pageable pageable);

    List<AdminPostSummaryDto> find(User currentUser, PostSearch postSearch, Pageable pageable);

    List<String> findAllDisplayNames();

    Long count();

    Long count(PostSearch postSearch);

    List<Post> findScheduledToBePublished();

    List<Post> findScheduledToBeIndexed(LocalDateTime indicesLastUpdatedAt);

    void disableSplashPosts();

    void disableFeaturedPosts();

    void flush();

    void detach(Post post);
}
