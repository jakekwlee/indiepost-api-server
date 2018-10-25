package com.indiepost.repository.jpa

import com.indiepost.enums.Types.UserRole
import com.indiepost.model.QRole
import com.indiepost.model.Role
import com.indiepost.repository.RoleRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 8/4/16.
 */
@Repository
class RoleRepositoryJpa : RoleRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val r = QRole.role

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(role: UserRole) {
        entityManager.persist(role)
    }

    override fun update(role: UserRole) {
        entityManager.persist(role)
    }

    override fun delete(role: UserRole) {
        entityManager.remove(role)
    }

    override fun findById(id: Long?): Role? {
        return queryFactory
                .selectFrom(r)
                .where(r.id.eq(id))
                .fetchOne()
    }

    override fun findByUserRole(role: UserRole): Role? {
        return findByUserRoleString(role.toString())
    }

    override fun findByUserRoleString(role: String): Role? {
        val userRole = UserRole.valueOf(role)
        return queryFactory
                .selectFrom(r)
                .where(r.roleType.eq(userRole))
                .fetchOne()
    }
}
