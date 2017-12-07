package com.indiepost.service;

public interface PostScheduledTaskService {

    void publishScheduledPosts();

    void updateElasticsearchIndices();

    void updateCachedPostStats();
}
