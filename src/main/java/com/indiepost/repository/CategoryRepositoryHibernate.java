package com.indiepost.repository;

import com.indiepost.model.Category;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class CategoryRepositoryHibernate implements CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Category category) {
        getSession().save(category);
    }

    @Override
    public void update(Category category) {
        getSession().update(category);
    }

    @Override
    public void delete(Category category) {
        getSession().delete(category);
    }

    @Override
    public Category getReference(Long id) {
        return entityManager.getReference(Category.class, id);
    }

    @Override
    public Category findById(Long id) {
        return (Category) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Category findBySlug(String slug) {
        return (Category) getCriteria()
                .add(Restrictions.eq("slug", slug))
                .uniqueResult();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return getCriteria()
                .add(Restrictions.eq("parentId", parentId))
                .list();
    }

    @Override
    public List<Category> findAll() {
        return getCriteria()
                .addOrder(Order.asc("displayOrder"))
                .list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Category.class);
    }
}