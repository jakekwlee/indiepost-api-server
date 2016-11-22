package com.indiepost.repository;

import com.indiepost.enums.PostEnum.Status;
import com.indiepost.model.Post;
import com.indiepost.repository.helper.CriteriaMaker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryHibernate implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaMaker criteriaMaker;

    @Override
    public Post findById(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Long save(Post post) {
        return (Long) getSession().save(post);
    }

    @Override
    public void delete(Post post) {
        getSession().delete(post);
    }

    @Override
    public void update(Post post) {
        getSession().update(post);
    }


    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(Status status) {
        return (Long) getCriteria()
                .add(Restrictions.eq("status", status))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(Status status, String authorName) {
        return (Long) getCriteria()
                .add(Restrictions.eq("authorName", authorName))
                .add(Restrictions.eq("status", status))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(Status status, Long authorId) {
        return (Long) getCriteria()
                .createAlias("author", "a")
                .add(Restrictions.eq("a.id", authorId))
                .add(Restrictions.eq("status", status))
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Post> findAll(Status status, Long authorId, Long categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findAll(Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByCategoryId(Long categoryId, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("c.id", categoryId))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByCategorySlug(String slug, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("c.slug", slug))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByAuthorId(Long authorId, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("a.id", authorId))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByAuthorName(String authorName, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("authorName", authorName))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByTagId(Long tagId, Pageable pageable) {
        return getCriteria(pageable)
                .createAlias("tags", "t")
                .add(Restrictions.eq("tags.id", tagId))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Post> findByTagName(String tagName, Pageable pageable) {
        return getCriteria(pageable)
                .createAlias("tags", "t")
                .add(Restrictions.eq("t.name", tagName))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    @Override
    public List<Post> findByStatus(Status status, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("status", status))
                .add(Restrictions.eq("status", Status.PUBLISHED))
                .list();
    }

    public List<Post> findPostToPublish() {
        return getCriteria()
                .add(Restrictions.eq("status", Status.BOOKED))
                .add(Restrictions.le("publishedAt", new Date())).list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }
}