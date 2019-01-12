package com.indiepost.repository.jpa

import com.indiepost.model.Bookmark
import com.indiepost.model.Post
import com.indiepost.model.QBookmark
import com.indiepost.model.User
import com.indiepost.repository.BookmarkRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class BookmarkRepositoryJpa : BookmarkRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(bookmark: Bookmark) {
        entityManager.persist(bookmark)
    }

    override fun create(userId: Long, postId: Long) {
        var bookmark = findOneByUserIdAndPostId(userId, postId)
        if (bookmark != null) {
            return
        }
        val userRef = entityManager.getReference(User::class.java, userId)
        val postRef = entityManager.getReference(Post::class.java, postId)
        bookmark = Bookmark(user = userRef, post = postRef, created = LocalDateTime.now())
        save(bookmark)
    }

    override fun delete(bookmark: Bookmark) {
        entityManager.remove(bookmark)
    }

    override fun findOneByUserIdAndPostId(userId: Long, postId: Long): Bookmark? {
        val b = QBookmark.bookmark
        return queryFactory
                .selectFrom(b)
                .where(b.userId.eq(userId).and(b.postId.eq(postId)))
                .fetchOne()
    }

    override fun findByUserIdAndPostIds(userId: Long, postIds: List<Long>): List<Bookmark> {
        val b = QBookmark.bookmark
        return queryFactory
                .selectFrom(b)
                .where(b.userId.eq(userId).and(b.postId.`in`(postIds)))
                .fetch()
    }

    override fun removeAllBookmarksByUserId(userId: Long) {
        val b = QBookmark.bookmark
        queryFactory
                .delete(b)
                .where(b.userId.eq(userId))
                .execute()
    }
}
