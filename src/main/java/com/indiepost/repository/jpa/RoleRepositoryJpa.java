package com.indiepost.repository.jpa;

import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.QRole;
import com.indiepost.model.Role;
import com.indiepost.repository.RoleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 8/4/16.
 */
@Repository
public class RoleRepositoryJpa implements RoleRepository {

    static final QRole r = QRole.role;

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
        return getQueryFactory()
                .selectFrom(r)
                .where(r.id.eq(id))
                .fetchOne();
    }

    @Override
    public Role findByUserRole(UserRole role) {
        return findByUserRoleString(role.toString());
    }

    @Override
    public Role findByUserRoleString(String role) {
        return getQueryFactory()
                .selectFrom(r)
                .where(r.name.eq(role))
                .fetchOne();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
