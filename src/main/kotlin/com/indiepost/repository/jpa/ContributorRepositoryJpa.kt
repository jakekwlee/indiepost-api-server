package com.indiepost.repository.jpa

import com.indiepost.enums.Types
import com.indiepost.model.Contributor
import com.indiepost.model.QContributor
import com.indiepost.repository.ContributorRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ContributorRepositoryJpa : ContributorRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val c = QContributor.contributor

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(contributor: Contributor): Long? {
        entityManager.persist(contributor)
        return contributor.id
    }

    override fun delete(contributor: Contributor) {
        entityManager.remove(contributor)
    }

    override fun deleteById(id: Long) {
        val contributor = findById(id)
        if (contributor != null) {
            entityManager.remove(contributor)
        }
    }

    override fun findById(id: Long): Contributor? {
        return queryFactory
                .selectFrom(c)
                .where(c.id.eq(id))
                .fetchOne()
    }

    override fun count(): Long {
        return queryFactory
                .selectFrom(c)
                .fetchCount()
    }

    override fun findAll(pageable: Pageable): Page<Contributor> {
        val contributorList = queryFactory
                .selectFrom(c)
                .orderBy(c.fullName.asc())
                .fetch()
        val count = queryFactory
                .selectFrom(c)
                .fetchCount()
        return PageImpl(contributorList, pageable, count)
    }

    override fun findAllByContributorType(contributorType: Types.ContributorType, pageable: Pageable): Page<Contributor> {
        val contributorList = queryFactory
                .selectFrom(c)
                .where(c.contributorType.eq(contributorType))
                .orderBy(c.fullName.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = countAllByContributorType(contributorType)
        return PageImpl(contributorList, pageable, count)
    }

    override fun findAllByFullName(fullName: String, pageable: Pageable): Page<Contributor> {
        val contributorList = queryFactory
                .selectFrom(c)
                .where(c.fullName.like(fullName))
                .orderBy(c.fullName.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = queryFactory
                .selectFrom(c)
                .where(c.fullName.like(fullName))
                .fetchCount()
        return PageImpl(contributorList, pageable, count)
    }

    override fun findByIdIn(ids: List<Long>, pageable: Pageable): Page<Contributor> {
        val contributorList = queryFactory
                .selectFrom(c)
                .where(c.id.`in`(ids))
                .orderBy(c.id.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = queryFactory
                .selectFrom(c)
                .where(c.id.`in`(ids))
                .fetchCount()
        return PageImpl(contributorList, pageable, count)
    }

    override fun findByFullNameIn(contributorNames: List<String>, pageable: Pageable): Page<Contributor> {
        val contributorList = queryFactory
                .selectFrom(c)
                .where(c.fullName.`in`(contributorNames))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val count = queryFactory
                .selectFrom(c)
                .where(c.fullName.`in`(contributorNames))
                .fetchCount()

        val result = ArrayList<Contributor>()
        for (name in contributorNames) {
            for (contributor in contributorList) {
                if (name == contributor.fullName) {
                    result.add(contributor)
                }
            }
        }
        return PageImpl(result, pageable, count)
    }

    override fun countAllByContributorType(contributorType: Types.ContributorType): Long {
        return queryFactory
                .selectFrom(c)
                .where(c.contributorType.eq(contributorType))
                .fetchCount()
    }
}
