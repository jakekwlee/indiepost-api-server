package com.indiepost.service;

/**
 * Indiepost Scheduled Tasks
 *
 * @deprecated All the scheduled tasks are replaced by AWS Lambda functions.
 */
@Deprecated
public interface PostScheduledTaskService {

    void publishScheduledPosts();

    void rebuildElasticsearchIndices();

    void updateCachedPostStats();
}
