package com.indiepost.repository;

import com.indiepost.model.StatLog;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 4. 9.
 */
@Repository
public class StatLogRepositoryHibernate implements StatLogRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(StatLog statLog) {
        getSession().save(statLog);
    }

    @Override
    public void update(StatLog statLog) {
        getSession().update(statLog);
    }

    @Override
    public StatLog findById(Long id) {
        return entityManager.find(StatLog.class, id);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(StatLog.class);
    }
}
