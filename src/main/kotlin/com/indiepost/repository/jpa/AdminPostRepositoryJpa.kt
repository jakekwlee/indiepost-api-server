package com.indiepost.repository.jpa

import com.indiepost.dto.post.AdminPostSummaryDto
import com.indiepost.dto.post.PostQuery
import com.indiepost.dto.post.Title
import com.indiepost.enums.Types
import com.indiepost.enums.Types.PostStatus
import com.indiepost.model.*
import com.indiepost.model.QPost.post
import com.indiepost.repository.AdminPostRepository
import com.indiepost.repository.utils.CriteriaUtils.addConjunction
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 17. 1. 11.
 */
@Repository
class AdminPostRepositoryJpa : AdminPostRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun getTitleList(): List<Title> {
        val result = queryFactory
                .select(post.id, post.title)
                .from(post)
                .where(post.status.eq(PostStatus.PUBLISH))
                .fetch()
        return result
                .stream()
                .map { row -> Title(row.get(post.id), row.get(post.title)) }
                .collect(Collectors.toList())
    }

    override fun persist(post: Post): Long? {
        if (post.categoryId != null) {
            val categoryReference = entityManager.getReference(Category::class.java, post.categoryId)
            post.category = categoryReference
        }
        if (post.titleImageId != null) {
            val titleImageReference = entityManager.getReference(ImageSet::class.java, post.titleImageId)
            post.titleImage = titleImageReference
        }
        entityManager.persist(post)
        return post.id
    }

    override fun findOne(id: Long?): Post? {
        return queryFactory
                .selectFrom(post)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne()
    }

    override fun delete(post: Post) {
        entityManager.remove(post)
    }

    override fun deleteById(id: Long?) {
        val post = entityManager.getReference(Post::class.java, id)
        entityManager.remove(post)
    }

    override fun isExists(id: Long?): Boolean {
        val count = queryFactory
                .selectFrom(post)
                .where(post.id.eq(id))
                .fetchCount()
        return count == 0L
    }

    override fun findByIdIn(ids: List<Long>): MutableList<AdminPostSummaryDto> {
        val query = queryFactory.selectFrom(post)
        addProjections(query)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .distinct()

        val builder = BooleanBuilder()
        builder.and(post.id.`in`(ids))

        query.where(builder)
        val rows = query.fetch()
        if (rows.size == 0) {
            return Collections.emptyList()
        }
        val posts = toDtoList(rows as MutableList<Tuple>)
        val ret = ArrayList<AdminPostSummaryDto>()
        for (id in ids) {
            for (post in posts) if (post.id == id) {
                ret.add(post)
            }
        }
        return ret
    }

    override fun find(currentUser: User, pageable: Pageable): List<AdminPostSummaryDto> {
        return Collections.emptyList()
    }

    override fun find(currentUser: User, status: PostStatus, pageable: Pageable): List<AdminPostSummaryDto> {
        val query = queryFactory.selectFrom(post)
        addProjections(query)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .orderBy(post.publishedAt.desc())
                .limit(pageable.pageSize.toLong())
                .offset(pageable.offset)
                .distinct()

        val builder = BooleanBuilder()
        builder.and(post.status.eq(status))
        addPrivacyCriteria(builder, status, currentUser)

        query.where(builder)
        return toDtoList(query.fetch() as MutableList<Tuple>)
    }

    override fun findAll(): List<Post> {
        return queryFactory
                .selectFrom(post)
                .where(post.id.goe(0))
                .orderBy(post.id.asc())
                .fetch()
    }

    override fun findByIds(ids: List<Long>): List<Post> {
        if (ids.isEmpty()) {
            return emptyList()
        }
        val posts = queryFactory
                .selectFrom(post)
                .where(post.id.`in`(ids))
                .fetch()
        val ret = ArrayList<Post>()
        for (id in ids) {
            for (post in posts) if (post.id == id) {
                ret.add(post)
            }
        }
        return ret
    }

    override fun findText(text: String, currentUser: User, status: PostStatus, pageable: Pageable): Page<AdminPostSummaryDto> {
        val like = "%$text%"
        val query = queryFactory
                .selectDistinct(post.id)
                .from(post)
                .leftJoin(post.postProfile, QPostProfile.postProfile)
                .leftJoin(QPostContributor.postContributor.contributor, QContributor.contributor)
                .leftJoin(post.postTags, QPostTag.postTag)
                .leftJoin(QPostTag.postTag.tag, QTag.tag)
                .orderBy(post.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())

        val builder = BooleanBuilder()
        builder.and(post.status.eq(status))
        addPrivacyCriteria(builder, status, currentUser)
        builder.and(post.title.like(like)
                .or(post.excerpt.like(like))
                .or(post.displayName.like(like))
                .or(QTag.tag.name.like(like))
                .or(QContributor.contributor.fullName.like(like))
        )

        query.where(builder)

        val count = query.fetchCount()
        val ids = query.fetch()

        if (ids.size == 0) {
            return PageImpl(emptyList(), pageable, 0)
        }

        val dtoList = findByIdIn(ids)
        return PageImpl(dtoList, pageable, count.toInt().toLong())
    }

    override fun findIds(currentUser: User, status: PostStatus): List<Long> {
        val query = queryFactory.selectFrom(post)
                .select(post.id)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .distinct()

        val builder = BooleanBuilder()
        builder.and(post.status.eq(status))
        addPrivacyCriteria(builder, status, currentUser)

        query.where(builder)
        return query.fetch()
    }

    override fun findAllDisplayNames(): List<String> {
        return queryFactory
                .from(post)
                .select(post.displayName)
                .where(post.displayName.isNotEmpty)
                .distinct()
                .fetch()
    }

    override fun count(): Long {
        return queryFactory
                .selectFrom(post)
                .fetchCount()
    }

    override fun count(postQuery: PostQuery): Long {
        val query = queryFactory.selectFrom(post)
        val builder = addConjunction(postQuery, BooleanBuilder())
        query.where(builder)
        return query.fetchCount()
    }

    override fun count(status: PostStatus, currentUser: User): Long {
        val query = queryFactory.from(post)

        val builder = BooleanBuilder()
        builder.and(post.status.eq(status))
        addPrivacyCriteria(builder, status, currentUser)

        query.where(builder)
        return query.fetchCount()
    }

    override fun findScheduledToBePublished(): List<Post> {
        return queryFactory
                .selectFrom(post)
                .where(post.status.eq(PostStatus.FUTURE), post.publishedAt.before(LocalDateTime.now()))
                .fetch()
    }

    override fun disableSplashPosts() {
        queryFactory
                .update(post)
                .set(post.isSplash, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.isSplash.eq(true))
                .execute()
    }

    override fun disableFeaturedPosts() {
        queryFactory
                .update(post)
                .set(post.isFeatured, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.isFeatured.eq(true))
                .execute()
    }

    private fun addProjections(query: JPAQuery<*>): JPAQuery<*> {
        return query.select(
                post.id, post.originalId, post.title, post.displayName, post.isSplash, post.isFeatured, post.isPicked,
                post.category.name, post.author.displayName, post.editor.displayName,
                post.createdAt, post.modifiedAt, post.publishedAt, post.status
        )
    }

    private fun addPrivacyCriteria(builder: BooleanBuilder, status: PostStatus, currentUser: User) {
        // TODO
        when (currentUser.roleType) {
            Types.UserRole.Administrator -> return
            Types.UserRole.EditorInChief, Types.UserRole.Editor -> {
                if (status == PostStatus.PUBLISH ||
                        status == PostStatus.FUTURE ||
                        status == PostStatus.PENDING) {
                    return
                }
                builder.and(post.editorId.eq(currentUser.id))
                return
            }
            else -> builder.and(post.authorId.eq(currentUser.id))
        }
    }

    private fun toDtoList(result: MutableList<Tuple>?): MutableList<AdminPostSummaryDto> {
        if (result == null) {
            return ArrayList()
        }
        val dtoList = ArrayList<AdminPostSummaryDto>()
        for (row in result) {
            val dto = AdminPostSummaryDto()
            dto.id = row.get(post.id)
            dto.originalId = row.get(post.originalId)
            dto.title = row.get(post.title)
            dto.displayName = row.get(post.displayName)
            row.get(post.isSplash)?.let {
                dto.isSplash = it
            }
            row.get(post.isFeatured)?.let {
                dto.isFeatured = it
            }
            row.get(post.isPicked)?.let {
                dto.isPicked = it
            }
            row.get(post.createdAt)?.let {
                dto.createdAt = localDateTimeToInstant(it)
            }
            row.get(post.modifiedAt)?.let {
                dto.modifiedAt = localDateTimeToInstant(it)
            }
            row.get(post.publishedAt)?.let {
                dto.publishedAt = localDateTimeToInstant(it)
            }
            dto.categoryName = row.get(post.category.name)
            dto.authorDisplayName = row.get(post.author.displayName)
            dto.editorDisplayName = row.get(post.editor.displayName)
            dto.status = row.get(post.status).toString()
            val postProfileList = row.get(post.postProfile)
            if (postProfileList != null) {
                val profiles = postProfileList.stream()
                        .map<String> { postProfile -> postProfile.profile?.fullName }
                        .collect(Collectors.toList())
                dto.profiles = profiles
            }
            val postTags = row.get(post.postTags)
            if (postTags != null) {
                val tags = postTags.stream()
                        .map<String> { postTag -> postTag.tag?.name }
                        .collect(Collectors.toList())
                dto.tags = tags
            }
            dtoList.add(dto)
        }
        return dtoList
    }
}
