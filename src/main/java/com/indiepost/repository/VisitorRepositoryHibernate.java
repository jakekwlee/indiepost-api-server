package com.indiepost.repository;

import com.indiepost.model.analytics.Visitor;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 4. 9.
 */
@Repository
public class VisitorRepositoryHibernate implements VisitorRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Visitor visitor) {
        return (Long) getSession().save(visitor);
    }

    @Override
    public void update(Visitor visitor) {
        getSession().update(visitor);
    }

    @Override
    public Visitor findById(Long id) {
        return entityManager.find(Visitor.class, id);
    }

    private org.hibernate.Session getSession() {
        return entityManager.unwrap(org.hibernate.Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Visitor.class);
    }
}
