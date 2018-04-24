package com.indiepost.repository;

import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.QRole;
import com.indiepost.model.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 8/4/16.
 */
@Repository
public class RoleRepositoryHibernate implements RoleRepository {

    static final QRole qRole = QRole.role;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(UserRole role) {
        entityManager.persist(role);
    }

    @Override
    public void update(UserRole role) {
        entityManager.persist(role);
    }

    @Override
    public void delete(UserRole role) {
        entityManager.remove(role);
    }

    @Override
    public Role findById(Long id) {
        return getQueryFactory().selectFrom(qRole).where(qRole.id.eq(id)).fetchOne();
    }

    @Override
    public Role findByUserRole(UserRole role) {
        return findByUserRoleString(role.toString());
    }

    @Override
    public Role findByUserRoleString(String role) {
        return getQueryFactory().selectFrom(qRole).where(qRole.name.eq(role)).fetchOne();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
