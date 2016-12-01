package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.PostResponse;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    Long save(Post post);

    PostResponse createAutosave(PostRequest postRequest);

    void updateAutosave(Long id, PostRequest postRequest);

    PostResponse update(Long id, PostRequest postRequest);

    PostResponse getPostResponse(Long id);

    void publishPosts();

    Post findById(Long id);

    void update(Post post);

    void delete(Post post);

    Long count();

    Long count(PostEnum.Status status);

    List<Post> findAll(int page, int maxResults, boolean isDesc);

    List<Post> findAll(PostEnum.Status status, User author, Category category, int page, int maxResults, boolean isDesc);

    List<Post> findByCategory(Category category, int page, int maxResults, boolean isDesc);

    List<Post> findByCategorySlug(String slug, int page, int maxResults, boolean isDesc);

    List<Post> findByAuthor(User author, int page, int maxResults, boolean isDesc);

    List<Post> findByAuthorName(String authorName, int page, int maxResults, boolean isDesc);

    List<Post> findByStatus(PostEnum.Status status, int page, int maxResults, boolean isDesc);

    List<Post> findByTag(Tag tag, int page, int maxResults, boolean isDesc);

    List<Post> findByTagName(String tagName, int page, int maxResults, boolean isDesc);
}