package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.post.PostResponse;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    Long save(Post post);

    Long saveDraft(PostRequest postRequest);

    PostResponse save(Long id, PostRequest postRequest);

    Post findById(Long id);

    PostResponse getPostResponse(Long id);

    void publishPosts();

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