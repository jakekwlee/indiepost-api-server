package com.indiepost.service;

public interface UserReadingService {

    void addUserReading(Long userId, Long postId);

    void setUserReadingInvisible(Long userId, Long postId);

    void setAllUserReadingInvisible(Long userId);

    void addBookmark(Long userId, Long postId);

    void removeBookmark(Long userId, Long postId);
}
