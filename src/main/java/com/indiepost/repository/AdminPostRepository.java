package com.indiepost.repository;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 17. 1. 10.
 */
public interface AdminPostRepository {

    Long persist(Post post);

    Post findOne(Long id);

    void delete(Post post);

    void deleteById(Long id);

    List<AdminPostSummaryDto> findByIdIn(List<Long> id);

    List<AdminPostSummaryDto> find(User currentUser, Pageable pageable);

    List<AdminPostSummaryDto> find(User currentUser, Types.PostStatus status, Pageable pageable);

    List<Long> findIds(User currentUser, Types.PostStatus status);

    List<String> findAllDisplayNames();

    Long count();

    Long count(PostQuery postQuery);

    Long count(Types.PostStatus status, User currentUser);

    List<Post> findScheduledToBePublished();

    void disableSplashPosts();

    void disableFeaturedPosts();
}
