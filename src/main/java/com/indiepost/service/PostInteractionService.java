package com.indiepost.service;

import com.indiepost.dto.post.PostInteractionDto;
import com.indiepost.model.PostInteraction;

public interface PostInteractionService {

    Long add(Long userId, Long postId);

    PostInteraction findOne(Long id);

    PostInteractionDto findUsersByPostId(Long postId);

    PostInteraction findOneByPostId(Long postId);

    void setInvisible(Long postId);

    void setInvisibleAll();

    void setVisibleAll();

    void setBookmark(Long postId);

    void unsetBookmark(Long postId);

    void clearAllBookmarks();

    void deleteById(Long id);
}
