package com.indiepost.repository;

import com.indiepost.model.Bookmark;

public interface BookmarkRepository {
    void save(Bookmark bookmark);

    void create(Long userId, Long postId);

    void delete(Bookmark bookmark);

    Bookmark findOneByUserIdAndPostId(Long userId, Long postId);

    void removeAllBookmarksByUserId(Long userId);
}
