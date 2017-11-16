package com.indiepost.repository;

import com.indiepost.model.legacy.LegacyPost;
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
public class LegacyPostRepositoryHibernate implements LegacyPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(LegacyPost legacyPost) {
        return (Long) getSession().save(legacyPost);
    }

    @Override
    public void update(LegacyPost legacyPost) {
        getSession().update(legacyPost);
    }

    @Override
    public void delete(LegacyPost legacyPost) {
        getSession().delete(legacyPost);
    }

    @Override
    public LegacyPost findByNo(Long no) {
        return (LegacyPost) getCriteria()
                .add(Restrictions.eq("no", no))
                .uniqueResult();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(LegacyPost.class);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
