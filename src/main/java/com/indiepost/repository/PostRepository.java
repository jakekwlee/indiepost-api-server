package com.indiepost.repository;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    Long save(Post post);

    void update(Post post);

    void delete(Post post);

    Post findById(Long id);

    void detach(Post post);

    Long count();

    Long count(PostEnum.Status status);

    Long count(PostEnum.Status status, String authorName);

    Long count(PostEnum.Status status, Long authorId);

    List<Post> findByTagId(Long tagId, Pageable pageable);

    List<Post> findByTagName(String tagName, Pageable pageable);

    List<Post> findByStatus(PostEnum.Status status, Pageable pageable);

    List<Post> findByCategoryId(Long categoryId, Pageable pageable);

    List<Post> findByCategorySlug(String categorySlug, Pageable pageable);

    List<Post> findByAuthorId(Long authorId, Pageable pageable);

    List<Post> findByAuthorName(String authorName, Pageable pageable);

    List<Post> findAll(Pageable pageable);

    List<Post> findAll(PostEnum.Status status, Long authorId, Long categoryId, Pageable pageable);

    List<Post> findPostToPublish();
}