package com.indiepost.repository.hibernate;

import com.indiepost.model.legacy.Detaillist;
import com.indiepost.repository.LegacyDetailListRepository;
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
public class LegacyDetailListRepositoryHibernate implements LegacyDetailListRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Detaillist detaillist) {
        return (Long) getSession().save(detaillist);
    }

    @Override
    public void update(Detaillist detaillist) {
        getSession().update(detaillist);
    }

    @Override
    public void delete(Detaillist detaillist) {
        getSession().delete(detaillist);
    }

    @Override
    public Detaillist findByNo(Long no) {
        return (Detaillist) getCriteria().add(Restrictions.eq("no", no)).uniqueResult();
    }

    public List<Detaillist> findByParent(Long parent) {
        return getCriteria()
                .add(Restrictions.eq("parent", parent))
                .addOrder(Order.asc("iorder")).list();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Detaillist.class);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
