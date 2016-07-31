package com.indiepost.repository;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
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

    Post findByIdForUser(int id);

    List<Post> findByCategory(Category category, Pageable pageable);

    List<Post> findByCategoryForUser(Category category, Pageable pageable);

    List<Post> findByCategorySlugForUser(String slug, Pageable pageable);

    List<Post> findByAuthor(User author, Pageable pageable);

    List<Post> findByAuthorForUser(User author, Pageable pageable);

    List<Post> findByAuthorUsernameForUser(String username, Pageable pageable);

    List<Post> findByEditor(User editor, Pageable pageable);

    List<Post> findAll(Pageable pageable);

    List<Post> findAllForUser(Pageable pageable);
}