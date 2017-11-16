package com.indiepost.repository;

import com.indiepost.model.legacy.LegacyPostContent;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 11/22/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class LegacyPostContentRepositoryHibernate implements LegacyPostContentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(LegacyPostContent legacyPostContent) {
        return (Long) getSession().save(legacyPostContent);
    }

    @Override
    public void update(LegacyPostContent legacyPostContent) {
        getSession().update(legacyPostContent);
    }

    @Override
    public void delete(LegacyPostContent legacyPostContent) {
        getSession().delete(legacyPostContent);
    }

    @Override
    public LegacyPostContent findByNo(Long no) {
        return (LegacyPostContent) getCriteria().add(Restrictions.eq("no", no)).uniqueResult();
    }

    public List<LegacyPostContent> findByParent(Long parent) {
        return getCriteria()
                .add(Restrictions.eq("parent", parent))
                .addOrder(Order.asc("iorder")).list();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(LegacyPostContent.class);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
