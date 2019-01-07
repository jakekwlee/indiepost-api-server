package com.indiepost.repository.jpa

import com.indiepost.enums.Types
import com.indiepost.model.Post
import com.indiepost.model.Profile
import com.indiepost.model.QPost
import com.indiepost.model.QProfile
import com.indiepost.repository.PostMigrationRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PostMigrationRepositoryImpl : PostMigrationRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun selectAllPostsWhereNotProfileSet(): List<Post> {
        val p = QPost.post

        return queryFactory.selectFrom(p)
                .where(p.displayName.notEqualsIgnoreCase("indiepost").and(
                        p.status.eq(Types.PostStatus.PUBLISH)
                ))
                .orderBy(p.publishedAt.desc())
                .fetch()
    }

    override fun findProfileByEtc(text: String): Profile? {
        val profiles = queryFactory.selectFrom(QProfile.profile)
                .where(QProfile.profile.etc.likeIgnoreCase("%${text.trim()}%"))
                .distinct()
                .fetch()
        return if (profiles.size > 0) {
            profiles[0]
        } else {
            null
        }
    }
}