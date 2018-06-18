package com.indiepost.repository;

import com.indiepost.model.UserReading;

public interface UserReadingRepository {
    void save(UserReading userReading);

    void save(UserReading userReading, Long userId, Long postId);

    UserReading findOne(Long id);

    UserReading findOneByUserIdAndPostId(Long userId, Long postId);

    void setInvisibleAll(Long userId);

    void deleteById(Long id);

}
