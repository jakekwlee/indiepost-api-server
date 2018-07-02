package com.indiepost.repository;

import com.indiepost.model.PostReading;

import java.util.List;

public interface PostReadingRepository {
    void save(PostReading postReading);

    Long save(PostReading postReading, Long userId, Long postId);

    PostReading findOne(Long id);

    PostReading findOneByUserIdAndPostId(Long userId, Long postId);

    List<PostReading> findByUserIdAndPostIds(Long userId, List<Long> postIds);

    void setVisibility(Long userId, boolean visible);

    void deleteById(Long id);
}
