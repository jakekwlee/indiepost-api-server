package com.indiepost.repository;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    void save(Post post);

    void update(Post post);

    void delete(Post post);

    Post findById(int id);

    int count();

    int count(PostEnum.Status status);

    int count(PostEnum.Status status, String username);

    List<Post> findByTag(Tag tag, Pageable pageable);

    List<Post> findByTag(Tag tag, Pageable pageable, boolean condensed);

    List<Post> findByTagName(String tagName, Pageable pageable);

    List<Post> findByTagName(String tagName, Pageable pageable, boolean condensed);

    List<Post> findByStatus(PostEnum.Status status, Pageable pageable);

    List<Post> findByStatus(PostEnum.Status status, Pageable pageable, boolean condensed);

    List<Post> findByCategory(Category category, Pageable pageable);

    List<Post> findByCategory(Category category, Pageable pageable, boolean condensed);

    List<Post> findByCategorySlug(String categorySlug, Pageable pageable);

    List<Post> findByCategorySlug(String categorySlug, Pageable pageable, boolean condensed);

    List<Post> findByAuthor(User author, Pageable pageable);

    List<Post> findByAuthor(User author, Pageable pageable, boolean condensed);

    List<Post> findByAuthorName(String authorName, Pageable pageable);

    List<Post> findByAuthorName(String authorName, Pageable pageable, boolean condensed);

    List<Post> findAll(Pageable pageable);

    List<Post> findAll(Pageable pageable, boolean condensed);

    List<Post> findAll(PostEnum.Status status, User author, Category category, Pageable pageable);

    List<Post> findAll(PostEnum.Status status, User author, Category category, Pageable pageable, boolean condensed);
}