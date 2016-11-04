package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;

import java.util.List;

/**
 * Created by jake on 11/5/16.
 */
public interface PostExcerptService {

    Post findById(Long id);

    List<Post> findAll(int page, int maxResults, boolean isDesc);

    List<Post> findByTitleLikes(String searchString, int page, int maxResults, boolean isDesc);

    List<Post> findByContentLikes(String searchString, int page, int maxResults, boolean isDesc);

    List<Post> findByTitleLikesOrContentLikes(String searchString, int page, int maxResults, boolean isDesc);

    List<Post> findByTagName(String tagName, int page, int maxResults, boolean isDesc);

    List<Post> findByTagIds(List<Long> tagIds, int page, int maxResults, boolean isDesc);

    List<Post> findByStatus(PostEnum.Status status, int page, int maxResults, boolean isDesc);

    List<Post> findByCategorySlug(String categorySlug, int page, int maxResults, boolean isDesc);

    List<Post> findByCategoryId(String categoryId, int page, int maxResults, boolean isDesc);

    List<Post> findByAuthorName(String authorName, int page, int maxResults, boolean isDesc);

    List<Post> findByAuthorId(Long authorID, int page, int maxResults, boolean isDesc);

    List<Post> findByStatusAndAuthorId(PostEnum.Status status, Long authorId, int page, int maxResults, boolean isDesc);

    List<Post> findByConditions(PostEnum.Status status, Long authorId, Long categoryId, List<Long> tagIds, String searchText, int page, int maxResults, boolean isDesc);

}
