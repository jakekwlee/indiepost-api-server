package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    void save(Post post);

    Post findById(int id);

    void update(Post post);

    void delete(Post post);

    int count();

    int countPublished();

    int countBooked();

    int countQueued();

    int countDraft();

    int countDeleted();

    List<Post> findAll(int page, int maxResults);

    List<Post> findAll(PostEnum.Status status, User author, Category category, int page, int maxResults);

    List<Post> findAllOrderByAsc(int page, int maxResults);

    List<Post> findAllOrderByAsc(PostEnum.Status status, User author, Category category, int page, int maxResults);

    List<Post> findByCategory(Category category, int page, int maxResults);

    List<Post> findByCategoryOrderByAsc(Category category, int page, int maxResults);

    List<Post> findByCategorySlug(String slug, int page, int maxResults);

    List<Post> findByCategorySlugOrderByAsc(String slug, int page, int maxResults);

    List<Post> findByAuthor(User author, int page, int maxResults);

    List<Post> findByAuthorOrderByAsc(User author, int page, int maxResults);

    List<Post> findByAuthorName(String authorName, int page, int maxResults);

    List<Post> findByAuthorNameOrderByAsc(String authorName, int page, int maxResults);

    List<Post> findByStatus(PostEnum.Status status, int page, int maxResults);

    List<Post> findByStatusOrderByAsc(PostEnum.Status status, int page, int maxResults);

    List<Post> findByTag(Tag tag, int page, int maxResults);

    List<Post> findByTagOrderByAsc(Tag tag, int page, int maxResults);

    List<Post> findByTagName(String tagName, int page, int maxResults);

    List<Post> findByTagOrderByAsc(String tagName, int page, int maxResults);
}
