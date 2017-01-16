package com.indiepost.repository;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.dto.request.PostQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    void update(Post post);

    Post findById(Long id);

    Long count();

    Long count(PostQuery query);

    List<Post> find(Pageable pageable);

    List<Post> find(PostQuery query, Pageable pageable);

    List<Post> findByStatus(PostEnum.Status status, Pageable pageable);

}