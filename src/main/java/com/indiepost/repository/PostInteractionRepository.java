package com.indiepost.repository;

import com.indiepost.model.PostInteraction;

import java.util.List;

public interface PostInteractionRepository {
    void save(PostInteraction postInteraction);

    Long save(PostInteraction postInteraction, Long userId, Long postId);

    PostInteraction findOne(Long id);

    PostInteraction findOneByUserIdAndPostId(Long userId, Long postId);

    List<PostInteraction> findByUserIdAndPostIds(Long userId, List<Long> postIds);

    void setVisibility(Long userId, boolean visible);

    void deleteById(Long id);

}
