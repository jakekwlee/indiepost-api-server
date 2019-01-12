package com.indiepost.repository.jpa

import com.indiepost.enums.Types
import com.indiepost.model.*
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

    override fun selectAllPostsWherePrimaryTagNotSet(): List<Post> {
        val p = QPost.post
        return queryFactory.selectFrom(p).where(p.primaryTag.isNull).orderBy(p.id.asc()).fetch()
    }

    override fun selectATagByName(text: String): Tag {
        val t = QTag.tag
        return queryFactory.selectFrom(t).where(t.name.equalsIgnoreCase(text)).fetchOne()
                ?: throw RuntimeException("Tag not found: $text")
    }

    override fun isTagAttached(postId: Long, tagId: Long): Boolean {
        val pt = QPostTag.postTag
        return queryFactory.selectFrom(pt).where(pt.postId.eq(postId).and(pt.tagId.eq(tagId))).fetchCount() > 0
    }

    override fun addTagsToCategoriesIfNotExists() {
        val c = QCategory.category
        val t = QTag.tag
        val categories = queryFactory.selectFrom(c).orderBy(c.id.asc()).fetch()
        for (category in categories) {
            val categoryName = category.name
            if (categoryName.isNullOrBlank())
                continue
            val tag = queryFactory.selectFrom(t).where(t.name.equalsIgnoreCase(categoryName)).fetchOne()
            if (tag == null) {
                entityManager.persist(Tag(name = categoryName))
            }
        }
    }

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