package com.indiepost.repository;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 16. 10. 17.
 * Read only repository
 */
public interface PostExcerptRepository {

    Post findById(Long id);

    List<Post> findAll(Long userId, Pageable pageable);

    List<Post> findByTitleLikes(String searchString, Pageable pageable);

    List<Post> findByContentLikes(String searchString, Pageable pageable);

    List<Post> findByTitleLikesOrContentLikes(String searchString, Pageable pageable);

    List<Post> findByTagName(String tagName, Pageable pageable);

    List<Post> findByTagIds(List<Long> tagIds, Pageable pageable);

    List<Post> findByStatus(PostEnum.Status status, Pageable pageable);

    List<Post> findByCategorySlug(String categorySlug, Pageable pageable);

    List<Post> findByCategoryId(String categoryId, Pageable pageable);

    List<Post> findByAuthorName(String authorName, Pageable pageable);

    List<Post> findByAuthorId(Long authorID, Pageable pageable);

    List<Post> findByStatusAndAuthorId(PostEnum.Status status, Long authorId, Pageable pageable);

    List<Post> findByConditions(PostEnum.Status status, Long authorId, Long categoryId, List<Long> tagIds, String searchText, Pageable pageable);

    List<String> findAllAuthorNames();
}