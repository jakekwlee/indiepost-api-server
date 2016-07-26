package com.indiepost.dao.hibernate;

import com.indiepost.dao.PostDAO;
import com.indiepost.model.Post;
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
public class PostDAOHibernate implements PostDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Post getPostById(int id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<Post> getPosts() {
        return getCriteria().addOrder(Order.desc("id")).list();
    }

    @Override
    public List<Post> getPosts(int firstResult, int maxResults) {
        return getCriteria()
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByCategoryId(int categoryId) {
        return getCriteria()
                .add(Restrictions.eq("categoryId", categoryId))
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByCategoryId(int categoryId, int firstResult, int maxResults) {
        return getCriteria()
                .add(Restrictions.eq("categoryId", categoryId))
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByAuthorId(int authorId) {
        return getCriteria()
                .add(Restrictions.eq("authorId", authorId))
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByAuthorId(int authorId, int firstResult, int maxResults) {
        return getCriteria()
                .add(Restrictions.eq("authorId", authorId))
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByStaffId(int staffId) {
        return getCriteria()
                .add(Restrictions.eq("staffId", staffId))
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public List<Post> getPostsByStaffId(int staffId, int firstResult, int maxResults) {
        return getCriteria()
                .add(Restrictions.eq("staffId", staffId))
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .addOrder(Order.desc("id"))
                .list();
    }

    @Override
    public void add(Post post) {
        getSession().save(post);
    }

    @Override
    public void update(Post post) {
        getSession().update(post);
    }

    @Override
    public void delete(Post post) {
        getSession().delete(post);
    }

    @Override
    public int count() {
        return ((Long) getCriteria().setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }
}
