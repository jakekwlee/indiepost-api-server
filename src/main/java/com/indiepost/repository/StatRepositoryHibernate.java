package com.indiepost.repository;

import com.indiepost.model.Stat;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 4. 17.
 */
@Repository
@SuppressWarnings("unchecked")
public class StatRepositoryHibernate implements StatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Stat stat) {
        return (Long) getSession().save(stat);
    }

    @Override
    public void delete(Stat stat) {
        getSession().delete(stat);
    }

    @Override
    public Stat findById(Long id) {
        return entityManager.find(Stat.class, id);
    }

    @Override
    public void update(Stat stat) {
        getSession().update(stat);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
