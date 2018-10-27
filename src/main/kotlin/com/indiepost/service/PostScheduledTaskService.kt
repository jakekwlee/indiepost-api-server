package com.indiepost.service

/**
 * Indiepost Scheduled Tasks
 *
 */
@Deprecated("All the scheduled tasks are replaced by AWS Lambda functions.")
interface PostScheduledTaskService {

    fun publishScheduledPosts()

    fun rebuildElasticsearchIndices()

    fun updateCachedPostStats()
}
