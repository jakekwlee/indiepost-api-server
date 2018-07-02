package com.indiepost.service;

import com.indiepost.dto.post.PostInteractionDto;
import com.indiepost.model.Bookmark;
import com.indiepost.model.PostReading;

public interface PostInteractionService {

    Long add(Long userId, Long postId);

    PostReading findOne(Long id);

    PostInteractionDto findUsersByPostId(Long postId);

    PostReading findOneByPostId(Long postId);

    Bookmark findBookmark(Long userId, Long postId);

    void setInvisible(Long postId);

    void setInvisibleAll();

    void setVisibleAll();

    void addBookmark(Long postId);

    void removeBookmark(Long postId);

    void removeAllUsersBookmarks();

    void deleteById(Long id);
}
