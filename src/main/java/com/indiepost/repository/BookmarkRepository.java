package com.indiepost.repository;

import com.indiepost.model.Bookmark;

import java.util.List;

public interface BookmarkRepository {
    void save(Bookmark bookmark);

    void create(Long userId, Long postId);

    void delete(Bookmark bookmark);

    Bookmark findOneByUserIdAndPostId(Long userId, Long postId);

    List<Bookmark> findByUserIdAndPostIds(Long userId, List<Long> postIds);

    void removeAllBookmarksByUserId(Long userId);
}
