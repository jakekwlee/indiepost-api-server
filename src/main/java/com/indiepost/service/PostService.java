package com.indiepost.service;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    Post findById(int id);

    Post findByIdForUser(int id);

    void update(Post post);

    void delete(Post post);

    List<Post> findAll(int page, int maxResults);

    List<Post> findAllForUser(int page, int maxResults);

    List<Post> findAllOrderByAsc(int page, int maxResults);

    List<Post> findByCategory(Category category, int page, int maxResults);

    List<Post> findByCategoryForUser(Category category, int page, int maxResults);

    List<Post> findByCategoryOrderByAsc(Category category, int page, int maxResults);

    List<Post> findByCategorySlug(String slug, int page, int maxResults);

    List<Post> findByCategorySlugForUser(String slug, int page, int maxResults);

    List<Post> findByCategorySlugOrderByAsc(String slug, int page, int maxResults);

    List<Post> findByAuthor(User author, int page, int maxResults);

    List<Post> findByAuthorForUser(User author, int page, int maxResults);

    List<Post> findByAuthoUsernamerForUser(String username, int page, int maxResults);

    List<Post> findByAuthorOrderByAsc(User author, int page, int maxResults);

    List<Post> findByEditor(User editor, int page, int maxResults);

    List<Post> findByEditorByAsc(User editor, int page, int maxResults);

    List<Post> findByStatus(Post.Status status, int page, int maxResults);

    List<Post> findByStatusOrderByAsc(Post.Status status, int page, int maxResults);
}
