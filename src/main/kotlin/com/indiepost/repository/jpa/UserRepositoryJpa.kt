package com.indiepost.repository.jpa

import com.indiepost.enums.Types
import com.indiepost.enums.Types.UserGender
import com.indiepost.enums.Types.UserState
import com.indiepost.model.QUser
import com.indiepost.model.User
import com.indiepost.repository.UserRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 7/30/16.
 */
@Repository
class UserRepositoryJpa : UserRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val u = QUser.user

    override val totalUsers: Long
        get() = queryFactory
                .selectFrom(u)
                .fetchCount()

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(user: User) {
        entityManager.persist(user)
        entityManager.flush()
    }

    override fun delete(user: User) {
        entityManager.remove(user)
    }

    override fun findAll(pageable: Pageable): List<User> {
        return queryFactory
                .selectFrom(u)
                .orderBy(u.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
    }

    override fun findById(id: Long?): User? {
        return queryFactory
                .selectFrom(u)
                .where(u.id.eq(id))
                .fetchOne()
    }

    override fun findByUsername(username: String): User? {
        return queryFactory
                .selectFrom(u)
                .where(u.username.eq(username))
                .fetchOne()
    }

    override fun search(text: String, role: Types.UserRole, pageable: Pageable): List<User> {
        val searchText = "%" + text.toLowerCase() + "%"
        return queryFactory
                .selectFrom(u)
                .where(
                        u.roleType.eq(role)
                                .and(u.email.likeIgnoreCase(searchText)
                                        .or(u.displayName.likeIgnoreCase(searchText))
                                )
                )
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .orderBy(u.joinedAt.desc())
                .fetch()
    }

    override fun findByEmail(email: String): User? {
        return queryFactory
                .selectFrom(u)
                .where(u.email.eq(email))
                .fetchOne()
    }

    override fun findCurrentUser(): User? {
        val securityContext = SecurityContextHolder.getContext()
        val authentication = securityContext.authentication ?: return null
        val username = authentication.name
        return findByUsername(username)
    }

    override fun findByState(state: UserState, pageable: Pageable): List<User> {
        return queryFactory
                .selectFrom(u)
                .where(u.state.eq(state))
                .orderBy(u.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
    }

    override fun findByGender(gender: UserGender, pageable: Pageable): List<User> {
        return queryFactory
                .selectFrom(u)
                .where(u.gender.eq(gender))
                .orderBy(u.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
    }

    override fun findByUserRole(role: Types.UserRole, pageable: Pageable): List<User> {
        return queryFactory
                .selectFrom(u)
                .where(u.roleType.eq(role))
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .orderBy(u.joinedAt.desc())
                .fetch()
    }

    override fun getTotalUsers(role: Types.UserRole): Long? {
        return queryFactory
                .selectFrom(u)
                .where(u.roleType.eq(role))
                .fetchCount()
    }

    override fun getTotalUsers(from: LocalDateTime, to: LocalDateTime): Long? {
        return queryFactory
                .selectFrom(u)
                .where(u.joinedAt.between(from, to))
                .fetchCount()
    }

    override fun searchTotal(text: String, role: Types.UserRole): Long? {
        val searchText = "%" + text.toLowerCase() + "%"
        return queryFactory
                .selectFrom(u)
                .where(
                        u.roleType.eq(role)
                                .and(
                                        u.email.likeIgnoreCase(searchText)
                                                .or(u.displayName.likeIgnoreCase(searchText))
                                )
                )
                .fetchCount()
    }
}
