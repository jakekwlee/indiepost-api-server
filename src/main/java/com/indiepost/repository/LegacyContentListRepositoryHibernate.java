package com.indiepost.repository;

import com.indiepost.model.legacy.Contentlist;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 11/22/16.
 */
@Repository
public class LegacyContentListRepositoryHibernate implements LegacyContentListRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Contentlist contentlist) {
        return (Long) getSession().save(contentlist);
    }

    @Override
    public void update(Contentlist contentlist) {
        getSession().update(contentlist);
    }

    @Override
    public void delete(Contentlist contentlist) {
        getSession().delete(contentlist);
    }

    @Override
    public Contentlist findByNo(Long no) {
        return (Contentlist) getCriteria()
                .add(Restrictions.eq("no", no))
                .uniqueResult();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Contentlist.class);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
