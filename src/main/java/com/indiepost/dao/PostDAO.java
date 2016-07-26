package com.indiepost.dao;

import com.indiepost.model.Post;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostDAO {

    Post getPostById(int id);

    List<Post> getPosts();

    List<Post> getPosts(int firstResult, int maxResults);

    List<Post> getPostsByCategoryId(int categoryId);

    List<Post> getPostsByCategoryId(int categoryId, int firstResult, int maxResults);

    List<Post> getPostsByAuthorId(int authorId);

    List<Post> getPostsByAuthorId(int authorId, int firstResult, int maxResults);

    List<Post> getPostsByStaffId(int staffId);

    List<Post> getPostsByStaffId(int staffId, int firstResult, int maxResults);

    void add(Post post);

    void update(Post post);

    void delete(Post post);

    int count();
}
