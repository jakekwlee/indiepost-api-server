package com.indiepost.service

import com.indiepost.dto.post.PostUserInteraction
import com.indiepost.model.Bookmark
import com.indiepost.model.PostReading
import com.indiepost.repository.BookmarkRepository
import com.indiepost.repository.PostReadingRepository
import com.indiepost.repository.UserRepository
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.inject.Inject
import javax.transaction.Transactional

@Service
@Transactional
class PostUserInteractionServiceImpl @Inject constructor(
        private val postReadingRepository: PostReadingRepository,
        private val userRepository: UserRepository,
        private val bookmarkRepository: BookmarkRepository) : PostUserInteractionService {

    private val currentUserId: Long?
        get() {
            val (id) = userRepository.findCurrentUser() ?: return null
            return id
        }

    override fun add(userId: Long, postId: Long): Long {
        var postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId)
        val now = LocalDateTime.now()
        if (postReading != null) {
            postReading.lastRead = now
            postReading.increaseReadCount()
            postReading.isVisible = true
            return postReading.id!!
        }
        postReading = PostReading()
        postReading.created = now
        postReading.lastRead = now
        postReadingRepository.save(postReading, userId, postId)
        return postReading.id!!
    }

    override fun findOne(id: Long): PostReading? {
        return postReadingRepository.findOne(id)
    }

    override fun findUsersByPostId(postId: Long): PostUserInteraction? {
        val userId = currentUserId ?: return null
        val postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId)
        val bookmark = bookmarkRepository.findOneByUserIdAndPostId(userId, postId)
        val dto = PostUserInteraction(postId)
        if (postReading != null) {
            dto.lastRead = localDateTimeToInstant(postReading.lastRead!!)
        }
        if (bookmark != null) {
            dto.bookmarked = localDateTimeToInstant(bookmark.created!!)
        }
        return dto
    }

    override fun findOneByPostId(postId: Long): PostReading? {
        val userId = currentUserId ?: return null
        return postReadingRepository.findOneByUserIdAndPostId(userId, postId)
    }

    override fun findBookmark(userId: Long, postId: Long): Bookmark? {
        return bookmarkRepository.findOneByUserIdAndPostId(userId, postId)
    }

    override fun setInvisible(postId: Long) {
        val userId = currentUserId ?: return
        val postReading = postReadingRepository.findOneByUserIdAndPostId(userId, postId)
                ?: return // fail silently
        postReading.isVisible = false
    }

    override fun setInvisibleAll() {
        val userId = currentUserId ?: return
        postReadingRepository.setVisibility(userId, false)
    }

    override fun setVisibleAll() {
        val userId = currentUserId ?: return
        postReadingRepository.setVisibility(userId, true)
    }

    override fun addBookmark(postId: Long) {
        val userId = currentUserId ?: return
        bookmarkRepository.create(userId, postId)
    }

    override fun removeBookmark(postId: Long) {
        val userId = currentUserId ?: return
        val bookmark = bookmarkRepository.findOneByUserIdAndPostId(userId, postId)
        if (bookmark != null) {
            bookmarkRepository.delete(bookmark)
        }
    }

    override fun removeAllUsersBookmarks() {
        val userId = currentUserId ?: return
        bookmarkRepository.removeAllBookmarksByUserId(userId)
    }

    override fun deleteById(id: Long) {
        postReadingRepository.deleteById(id)
    }
}
