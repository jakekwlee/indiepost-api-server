package com.indiepost.repository.jpa

import com.indiepost.enums.Types
import com.indiepost.model.Profile
import com.indiepost.model.QProfile
import com.indiepost.repository.ProfileRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ProfileRepositoryJpa : ProfileRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val p = QProfile.profile

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(profile: Profile): Long? {
        entityManager.persist(profile)
        return profile.id
    }

    override fun delete(profile: Profile) {
        entityManager.remove(profile)
    }

    override fun deleteById(id: Long) {
        val profile = findById(id)
        if (profile != null) {
            entityManager.remove(profile)
        }
    }

    override fun findById(id: Long): Profile? {
        return queryFactory
                .selectFrom(p)
                .where(p.id.eq(id))
                .fetchOne()
    }

    override fun findBySlug(slug: String): Profile? {
        return queryFactory
                .selectFrom(p)
                .where(p.slug.eq(slug))
                .fetchOne()
    }

    override fun count(): Long {
        return queryFactory
                .selectFrom(p)
                .fetchCount()
    }

    override fun findAll(pageable: Pageable): Page<Profile> {
        val profileList = queryFactory
                .selectFrom(p)
                .orderBy(p.fullName.asc())
                .fetch()
        val count = queryFactory
                .selectFrom(p)
                .fetchCount()
        return PageImpl(profileList, pageable, count)
    }

    override fun findAllByProfileType(profileType: Types.ProfileType, pageable: Pageable): Page<Profile> {
        val profileList = queryFactory
                .selectFrom(p)
                .where(p.profileType.eq(profileType))
                .orderBy(p.fullName.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = countAllByProfileType(profileType)
        return PageImpl(profileList, pageable, count)
    }

    override fun findAllByFullName(fullName: String, pageable: Pageable): Page<Profile> {
        val profileList = queryFactory
                .selectFrom(p)
                .where(p.fullName.like(fullName))
                .orderBy(p.fullName.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = queryFactory
                .selectFrom(p)
                .where(p.fullName.like(fullName))
                .fetchCount()
        return PageImpl(profileList, pageable, count)
    }

    override fun findByIdIn(ids: List<Long>, pageable: Pageable): Page<Profile> {
        val profileList = queryFactory
                .selectFrom(p)
                .where(p.id.`in`(ids))
                .orderBy(p.id.asc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = queryFactory
                .selectFrom(p)
                .where(p.id.`in`(ids))
                .fetchCount()
        return PageImpl(profileList, pageable, count)
    }

    override fun findByFullNameIn(profileNames: List<String>, pageable: Pageable): Page<Profile> {
        val profileList = queryFactory
                .selectFrom(p)
                .where(p.fullName.`in`(profileNames))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val count = queryFactory
                .selectFrom(p)
                .where(p.fullName.`in`(profileNames))
                .fetchCount()

        val result = ArrayList<Profile>()
        for (name in profileNames) {
            for (profile in profileList) {
                if (name == profile.fullName) {
                    result.add(profile)
                }
            }
        }
        return PageImpl(result, pageable, count)
    }

    override fun countAllByProfileType(profileType: Types.ProfileType): Long {
        return queryFactory
                .selectFrom(p)
                .where(p.profileType.eq(profileType))
                .fetchCount()
    }
}
