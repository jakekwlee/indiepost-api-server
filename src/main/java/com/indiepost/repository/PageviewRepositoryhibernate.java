package com.indiepost.repository;

import com.indiepost.model.Pageview;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 17. 4. 17.
 */
@Repository
public class PageviewRepositoryhibernate implements PageviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Pageview pageview) {
        return (Long) getSession().save(pageview);
    }

    @Override
    public void delete(Pageview pageview) {
        getSession().delete(pageview);
    }

    @Override
    public Pageview findById(Long id) {
        return entityManager.find(Pageview.class, id);
    }

    @Override
    public void update(Pageview pageview) {
        getSession().update(pageview);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
