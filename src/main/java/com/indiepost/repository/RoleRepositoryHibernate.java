package com.indiepost.repository;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Role;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 8/4/16.
 */
@Repository
public class RoleRepositoryHibernate implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(UserEnum.Roles role) {
        getSession().save(role);
    }

    @Override
    public void update(UserEnum.Roles role) {
        getSession().update(role);
    }

    @Override
    public void delete(UserEnum.Roles role) {
        getSession().delete(role);
    }

    @Override
    public Role findById(Long id) {
        return (Role) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Role findByRolesEnum(UserEnum.Roles role) {
        return (Role) getCriteria()
                .createAlias("users", "users")
                .add(Restrictions.eq("name", role.toString()))
                .uniqueResult();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Role.class);
    }
}
