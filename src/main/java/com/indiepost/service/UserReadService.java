package com.indiepost.service;

import com.indiepost.model.UserRead;

public interface UserReadService {

    Long add(Long userId, Long postId);

    UserRead findOne(Long id);

    UserRead findOneByUserIdAndPostId(Long userId, Long postId);

    void setInvisible(Long userId, Long postId);

    void setInvisibleAllByUserId(Long userId);

    void setBookmark(Long userId, Long postId);

    void unsetBookmark(Long userId, Long postId);

    void deleteById(Long id);
}
