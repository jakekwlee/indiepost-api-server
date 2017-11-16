package com.indiepost.repository;

import com.indiepost.model.Tag;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.setPageToCriteria;

/**
 * Created by jake on 9/17/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class TagRepositoryHibernate implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Tag tag) {
        getSession().save(tag);
    }

    @Override
    public Tag findByTagName(String name) {
        return (Tag) getCriteria()
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }

    @Override
    public Tag findById(Long id) {
        return (Tag) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<Tag> findAll() {
        return getCriteria()
                .addOrder(Order.asc("name"))
                .list();
    }

    @Override
    public List<Tag> findAll(Pageable pageable) {
        return setPageToCriteria(getCriteria(), pageable)
                .list();
    }

    @Override
    public List<Tag> findByIds(List<Long> ids) {
        return getCriteria()
                .add(Restrictions.in("id", ids))
                .list();
    }

    @Override
    public void update(Tag tag) {
        getSession().update(tag);
    }

    @Override
    public void delete(Tag tag) {
        getSession().delete(tag);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Tag.class);
    }
}
