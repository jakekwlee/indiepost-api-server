package com.indiepost.repository;

import com.indiepost.model.UserRead;

public interface UserReadRepository {
    void save(UserRead userRead);

    Long save(UserRead userRead, Long userId, Long postId);

    UserRead findOne(Long id);

    UserRead findOneByUserIdAndPostId(Long userId, Long postId);

    void setInvisibleAll(Long userId);

    void deleteById(Long id);

}
