package com.indiepost.service;

import com.indiepost.model.UserReading;

public interface UserReadingService {

    Long add(Long userId, Long postId);

    UserReading findOne(Long id);

    UserReading findOneByUserIdAndPostId(Long userId, Long postId);

    void setInvisible(Long userId, Long postId);

    void setInvisibleAllByUserId(Long userId);

    void setVisibleAllByUserId(Long userId);

    void setBookmark(Long userId, Long postId);

    void unsetBookmark(Long userId, Long postId);

    void deleteById(Long id);
}
