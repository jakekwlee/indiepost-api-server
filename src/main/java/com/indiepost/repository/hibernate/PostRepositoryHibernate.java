package com.indiepost.repository.hibernate;

import com.indiepost.enums.PostEnum.Status;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.repository.CriteriaMaker;
import com.indiepost.repository.PostRepository;
import com.indiepost.util.AliasToBeanNestedResultTransformer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by jake on 7/30/16.
 */
@Repository
public class PostRepositoryHibernate implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaMaker criteriaMaker;

    private Map<String, String> alias = new HashMap<>();

    private Map<String, String> projections = new HashMap<>();

    @Override
    public Post findById(int id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public void save(Post post) {
        getSession().save(post);
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
    public List<Post> findAll(Pageable pageable) {
        return findAll(pageable, true);
    }

    @Override
    public List<Post> findAll(Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                .list();
    }

    @Override
    public List<Post> findAll(Status status, User author, Category category, Pageable pageable) {
        return findAll(status, author, category, pageable, true);
    }

    @Override
    public List<Post> findAll(Status status, User author, Category category, Pageable pageable, boolean condensed) {
        Criteria criteria =  getCriteria(pageable, condensed);
        if (!condensed) {
            criteria.createAlias("category", "c");
            criteria.createAlias("author", "a");
        }
        if (status != null) {
            criteria.add(Restrictions.eq("status", status));
        }
        if (category != null && category.getId() != 1) {
            criteria.add(Restrictions.eq("c.id", category.getId()));
        }
        if (author != null) {
             criteria.add(Restrictions.eq("a.id", author.getId()));
        }
        return criteria.list();
    }

    @Override
    public List<Post> findByCategory(Category category, Pageable pageable) {
        return findByCategory(category, pageable, true);
    }

    @Override
    public List<Post> findByCategory(Category category, Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                .add(Restrictions.eq("c.id", category.getId()))
                .list();
    }

    @Override
    public List<Post> findByCategorySlug(String slug, Pageable pageable) {
        return findByCategorySlug(slug, pageable, true);
    }

    @Override
    public List<Post> findByCategorySlug(String slug, Pageable pageable, boolean condensed) {
        Criteria criteria = getCriteria(pageable, condensed);
        if (!condensed) {
            criteria.createAlias("category", "c");
        }
        return criteria
                .add(Restrictions.eq("c.slug", slug))
                .list();
    }

    @Override
    public List<Post> findByAuthor(User author, Pageable pageable) {
        return findByAuthor(author, pageable, true);
    }

    @Override
    public List<Post> findByAuthor(User author, Pageable pageable, boolean condensed) {
        Criteria criteria = getCriteria(pageable, condensed);
        if (!condensed) {
            criteria.createAlias("author", "a");
        }
        return criteria
                .add(Restrictions.eq("a.id", author.getId()))
                .list();
    }

    @Override
    public List<Post> findByAuthorName(String authorName, Pageable pageable) {
        return findByAuthorName(authorName, pageable, true);
    }

    @Override
    public List<Post> findByAuthorName(String authorName, Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                .add(Restrictions.eq("authorName", authorName))
                .list();
    }

    @Override
    public List<Post> findByTag(Tag tag, Pageable pageable) {
        return findByTag(tag, pageable, true);
    }

    @Override
    public List<Post> findByTag(Tag tag, Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                .createAlias("tags", "t")
                .add(Restrictions.eq("tags.id", tag.getId()))
                .list();
    }

    @Override
    public List<Post> findByTagName(String tagName, Pageable pageable) {
        return findByTagName(tagName, pageable, true);
    }

    @Override
    public List<Post> findByTagName(String tagName, Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                .createAlias("tags", "t")
                .add(Restrictions.eq("t.name", tagName))
                .list();
    }

    @Override
    public List<Post> findByStatus(Status status, Pageable pageable) {
        return findByStatus(status, pageable, true);
    }

    @Override
    public List<Post> findByStatus(Status status, Pageable pageable, boolean condensed) {
        return getCriteria(pageable, condensed)
                        .add(Restrictions.eq("status", status))
                        .list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        alias.clear();
        projections.clear();
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable, boolean condensed) {
        if (condensed) {
            return getCriteriaWithProjections(pageable);
        }
        return getCriteria(pageable);
    }

    private Criteria getCriteria(Pageable pageable) {
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }

    private Criteria getCriteriaWithProjections(Pageable pageable) {
        Criteria criteria = getCriteria(pageable);

        alias.put("author", "a");
        alias.put("category", "c");

        projections.put("id", "id");
        projections.put("title", "title");
        projections.put("featuredImage", "featuredImage");
        projections.put("excerpt", "excerpt");
        projections.put("publishedAt", "publishedAt");
        projections.put("likesCount", "likesCount");
        projections.put("commentsCount", "commentsCount");
        projections.put("status", "status");
        projections.put("a.id", "author.id");
        projections.put("a.displayName", "author.displayName");
        projections.put("c.id", "category.id");
        projections.put("c.name", "category.name");
        projections.put("c.slug", "category.slug");

        setAlias(criteria);
        setProjections(criteria);
        criteria.add(Restrictions.eq("status", Status.PUBLISHED))
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class));

        return criteria;
    }

    private Criteria setAlias(Criteria criteria) {
        Iterator<String> keys = alias.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            criteria.createAlias(key, alias.get(key));
        }
        return criteria;
    }

    private Criteria setProjections(Criteria criteria) {
        Iterator<String> keys = projections.keySet().iterator();
        ProjectionList projectionList = Projections.projectionList();
        while (keys.hasNext()) {
            String key = keys.next();
            projectionList.add(Property.forName(key), projections.get(key));
        }
        return criteria.setProjection(projectionList);
    }
}
