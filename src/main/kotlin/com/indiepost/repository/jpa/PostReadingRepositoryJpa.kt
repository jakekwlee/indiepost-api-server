package com.indiepost.repository.jpa

import com.indiepost.model.Post
import com.indiepost.model.PostReading
import com.indiepost.model.QPostReading
import com.indiepost.model.User
import com.indiepost.repository.PostReadingRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PostReadingRepositoryJpa : PostReadingRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val interaction = QPostReading.postReading

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun findOne(id: Long?): PostReading? {
        return entityManager.find(PostReading::class.java, id)
    }

    override fun findOneByUserIdAndPostId(userId: Long, postId: Long): PostReading? {
        return queryFactory.selectFrom(interaction).where(
                interaction.userId.eq(userId).and(
                        interaction.postId.eq(postId))
        ).fetchOne()
    }

    override fun findByUserIdAndPostIds(userId: Long, postIds: List<Long>): List<PostReading> {
        return queryFactory.selectFrom(interaction)
                .where(interaction.userId.eq(userId).and(interaction.postId.`in`(postIds)))
                .orderBy(interaction.lastRead.desc())
                .fetch()
    }

    override fun save(postReading: PostReading) {
        entityManager.persist(postReading)
    }

    override fun save(postReading: PostReading, userId: Long, postId: Long): Long? {
        val userRef = entityManager.getReference(User::class.java, userId)
        val postRef = entityManager.getReference(Post::class.java, postId)
        postReading.user = userRef
        postReading.post = postRef
        save(postReading)
        entityManager.flush()
        return postReading.id
    }

    override fun setVisibility(userId: Long, visible: Boolean) {
        queryFactory.update(interaction).set(interaction.isVisible, visible)
                .where(interaction.userId.eq(userId).and(interaction.id.goe(1)))
                .execute()
    }

    override fun deleteById(id: Long) {
        val postReading = entityManager.getReference(PostReading::class.java, id)
        entityManager.remove(postReading)
    }
}
