package com.indiepost.repository;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
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

    void delete(Post post);

    void deleteById(Long id);

    void bulkDeleteByUserAndStatus(User currentUser, Types.PostStatus postStatus);

    List<AdminPostSummaryDto> findByIdIn(List<Long> id);

    List<AdminPostSummaryDto> find(User currentUser, Pageable pageable);

    List<AdminPostSummaryDto> find(User currentUser, PostQuery postQuery, Pageable pageable);

    List<String> findAllDisplayNames();

    Long count();

    Long count(PostQuery postQuery);

    List<Post> findScheduledToBePublished();

    List<Post> findScheduledToBeIndexed(LocalDateTime indicesLastUpdatedAt);

    void disableSplashPosts();

    void disableFeaturedPosts();

    void flush();
}
