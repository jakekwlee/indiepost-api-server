package com.indiepost.repository.jpa

import com.indiepost.dto.StaticPageDto
import com.indiepost.enums.Types
import com.indiepost.model.QStaticPage.staticPage
import com.indiepost.model.QUser.user
import com.indiepost.model.StaticPage
import com.indiepost.repository.StaticPageRepository
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 17. 3. 5.
 */
@Repository
class StaticPageRepositoryJpa : StaticPageRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(staticPage: StaticPage): Long? {
        entityManager.persist(staticPage)
        return staticPage.id
    }

    override fun findById(id: Long?): StaticPage {
        return entityManager.find(StaticPage::class.java, id)
    }

    override fun update(staticPage: StaticPage) {
        entityManager.persist(staticPage)
    }

    override fun delete(staticPage: StaticPage) {
        entityManager.remove(staticPage)
    }

    override fun find(pageable: Pageable): Page<StaticPageDto> {
        val p = staticPage
        val rows = queryFactory.select(
                p.id,
                p.title,
                p.status,
                p.title,
                p.createdAt,
                p.modifiedAt,
                p.displayOrder,
                p.author.displayName)
                .from(p)
                .innerJoin(user)
                .on(p.authorId.eq(user.id))
                .orderBy(p.displayOrder.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val dtoList = toDtoList(rows)
        return PageImpl(dtoList, pageable, count())
    }

    override fun find(pageable: Pageable, pageStatus: Types.PostStatus): Page<StaticPageDto> {
        val p = staticPage
        val rows = queryFactory.select(
                p.id,
                p.title,
                p.status,
                p.title,
                p.createdAt,
                p.modifiedAt,
                p.displayOrder,
                p.slug,
                p.author.displayName)
                .from(p)
                .innerJoin(user)
                .on(p.authorId.eq(user.id))
                .where(p.status.eq(pageStatus))
                .orderBy(p.displayOrder.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val dtoList = toDtoList(rows)
        val count = count(pageStatus)
        return PageImpl(dtoList, pageable, count)
    }

    override fun count(): Long {
        return queryFactory.selectFrom(staticPage).fetchCount()
    }

    override fun count(pageStatus: Types.PostStatus): Long {
        return queryFactory
                .selectFrom(staticPage)
                .where(staticPage.status.eq(pageStatus))
                .fetchCount()
    }

    override fun findBySlug(slug: String): StaticPage? {
        return queryFactory
                .selectFrom(staticPage)
                .where(staticPage.slug.eq(slug))
                .fetchOne()
    }

    override fun bulkUpdateStatusByIds(ids: List<Long>, status: Types.PostStatus) {
        queryFactory
                .update(staticPage)
                .set(staticPage.status, status)
                .where(staticPage.id.`in`(ids))
                .execute()
    }

    override fun bulkDeleteByIds(ids: List<Long>) {
        queryFactory
                .delete(staticPage)
                .where(staticPage.id.`in`(ids))
                .execute()
    }

    override fun bulkDeleteByStatus(status: Types.PostStatus) {
        if (status != Types.PostStatus.TRASH) {
            // TODO error handling
            return
        }
        queryFactory
                .delete(staticPage)
                .where(staticPage.status.eq(status))
                .execute()
    }

    override fun isExists(id: Long?): Boolean {
        return queryFactory
                .selectFrom(staticPage)
                .where(staticPage.id.eq(id))
                .fetchCount() > 1
    }

    private fun toDtoList(result: List<Tuple>): List<StaticPageDto> {
        return result.stream().map { row ->
            val dto = StaticPageDto()
            dto.id = row.get(staticPage.id)
            dto.title = row.get(staticPage.title)
            dto.authorDisplayName = row.get(staticPage.author.displayName)
            dto.createdAt = localDateTimeToInstant(row.get(staticPage.createdAt))
            dto.modifiedAt = localDateTimeToInstant(row.get(staticPage.modifiedAt))
            row.get(staticPage.displayOrder)?.let {
                dto.displayOrder = it
            }
            dto.slug = row.get(staticPage.slug)
            dto
        }.collect(Collectors.toList())
    }
}
