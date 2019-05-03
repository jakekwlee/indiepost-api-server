package com.indiepost.repository.jpa

import com.indiepost.dto.SelectedTagDto
import com.indiepost.enums.Types
import com.indiepost.model.QTag
import com.indiepost.model.QTagSelected
import com.indiepost.model.Tag
import com.indiepost.model.TagSelected
import com.indiepost.repository.TagRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 9/17/16.
 */
@Repository
class TagRepositoryJpa : TagRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val t = QTag.tag

    private val jpaQueryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(tag: Tag) {
        entityManager.persist(tag)
    }

    override fun findByTagName(name: String): Tag? {
        return jpaQueryFactory
                .selectFrom(t)
                .where(t.name.eq(name))
                .fetchOne()
    }

    override fun findById(id: Long?): Tag? {
        return jpaQueryFactory
                .selectFrom(t)
                .where(t.id.eq(id))
                .fetchOne()
    }

    override fun findSelected(): List<SelectedTagDto> {
        val t = QTag.tag
        val ts = QTagSelected.tagSelected
        val tupleList = jpaQueryFactory
                .select(t.id, t.name, ts.priority)
                .from(ts)
                .innerJoin(ts.tag, t)
                .orderBy(ts.priority.asc())
                .fetch()
        return tupleList.map {
            SelectedTagDto(id = it[t.id], text = it[t.name], priority = it[ts.priority])
        }
    }

    override fun updateSelected(tagNames: List<String>) {
        if (tagNames.isNullOrEmpty())
            return
        val ts = QTagSelected.tagSelected
        jpaQueryFactory.delete(ts).where(t.id.goe(0)).execute()
        val tags = findByNameIn(tagNames)
        for (tag in tags) {
            val selected = TagSelected(tag = tag)
            entityManager.persist(selected)
        }
    }

    override fun findAll(): List<Tag> {
        return jpaQueryFactory
                .selectFrom(t)
                .orderBy(t.name.asc())
                .fetch()
    }

    override fun findAll(pageable: Pageable): List<Tag> {
        return jpaQueryFactory
                .selectFrom(t)
                .orderBy(t.name.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
    }

    override fun findAll(pageable: Pageable, tagType: Types.TagType?, text: String?): List<Tag> {
        // TODO 05-01
        return emptyList()
    }

    override fun findByNameIn(tagNames: List<String>): List<Tag> {
        val tags = jpaQueryFactory
                .selectFrom(t)
                .where(t.name.`in`(tagNames))
                .fetch()
        val result = ArrayList<Tag>()
        for (name in tagNames) {
            for (tag in tags) {
                if (name.toLowerCase() == tag.name!!.toLowerCase()) {
                    result.add(tag)
                    break
                }
            }
        }
        return result
    }

    override fun delete(tag: Tag) {
        entityManager.remove(tag)
    }
}
