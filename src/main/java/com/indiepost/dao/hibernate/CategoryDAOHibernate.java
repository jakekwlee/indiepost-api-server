package com.indiepost.dao.hibernate;

import com.indiepost.dao.CategoryDAO;
import com.indiepost.model.Category;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */

@Repository
public class CategoryDAOHibernate implements CategoryDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Category getCagetoryById(int id) {

        return (Category) this.getCriteria().add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Category getCategoryBySlug(String slug) {
        return (Category) this.getCriteria().add(Restrictions.eq("slug", slug))
                .uniqueResult();
    }

    @Override
    public List<Category> getCategories() {
        return getCriteria()
                .addOrder(Order.asc("displayOrder"))
                .list();
    }

    @Override
    public List<Category> getCategoriesByParent(Category parent) {
        return getCriteria()
                .add(Restrictions.eq("parentId", parent.getId()))
                .addOrder(Order.asc("displayOrder"))
                .list();
    }

    @Override
    public void add(Category category) {
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
    public int count() {
        return ((Long) getCriteria().setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Category.class);
    }
}
