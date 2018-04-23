package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummary;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.buildConjunction;
import static com.indiepost.repository.utils.CriteriaUtils.setPageToCriteria;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryHibernate implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post findById(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(PostQuery query) {
        Conjunction conjunction = Restrictions.conjunction();
        buildConjunction(query, conjunction);
        return (Long) getCriteria().add(conjunction).setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<PostSummary> find(Pageable pageable) {
        return findByQuery(new PostQuery(), pageable);
    }


    @Override
    public List<PostSummary> findByQuery(PostQuery query, Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        ProjectionList projectionList = this.getProjectionList();
        if (query != null && StringUtils.isNotEmpty(query.getCategorySlug())) {
            criteria.createAlias("category", "category");
            projectionList.add(Property.forName("category.slug"), "categoryName");
        }
        criteria.setProjection(projectionList);
        Conjunction conjunction = Restrictions.conjunction();

        if (query != null) {
            buildConjunction(query, conjunction);
        }
        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }
        criteria.setResultTransformer(new FluentHibernateResultTransformer(PostSummary.class));

        return criteria.list();
    }

    @Override
    public List<PostSummary> findByCategoryId(Long categoryId, Pageable pageable) {
        PostQuery query = new PostQuery();
        if (categoryId != 0L) {
            query.setCategoryId(categoryId);
        }
        return this.findByQuery(query, pageable);
    }

    @SuppressWarnings("JpaQlInspection")
    @Override
    public List<PostSummary> findByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        String[] stringArray = ids.stream()
                .map(id -> id.toString())
                .toArray(String[]::new);

        String joinedIds = String.join(", ", stringArray);
        String queryString = "select new com.indiepost.dto.PostSummary(" +
                "p.id, p.featured, p.picked, p.splash, p.title, p.excerpt, " +
                "p.displayName, p.publishedAt, p.titleImage, p.titleImageId, p.status, c.id, c.name, " +
                "p.commentsCount, p.likesCount) from Post p inner join p.category c where p.id in (:ids) ORDER BY field(p.id, " + joinedIds + ")";
        org.hibernate.Query query = getSession().createQuery(queryString);
        query.setParameterList("ids", ids);
        return query.list();
    }

    @Override
    public List<PostSummary> findByStatus(PostStatus status, Pageable pageable) {
        PostQuery query = new PostQuery();
        query.setStatus(status);
        return this.findByQuery(query, pageable);
    }

    @Override
    public List<PostSummary> findScheduledPosts() {
        return getCriteria()
                .setProjection(getProjectionList())
                .add(Restrictions.eq("status", PostStatus.FUTURE))
                .addOrder(Order.asc("publishedAt"))
                .setResultTransformer(new FluentHibernateResultTransformer(PostSummary.class))
                .list();
    }

    private ProjectionList getProjectionList() {
        return Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("title"), "title")
                .add(Property.forName("featured"), "featured")
                .add(Property.forName("picked"), "picked")
                .add(Property.forName("excerpt"), "excerpt")
                .add(Property.forName("splash"), "splash")
                .add(Property.forName("publishedAt"), "publishedAt")
                .add(Property.forName("displayName"), "displayName")
                .add(Property.forName("commentsCount"), "commentsCount")
                .add(Property.forName("likesCount"), "likesCount")
                .add(Property.forName("categoryId"), "categoryId")
                .add(Property.forName("status"), "status")
                .add(Property.forName("titleImageId"), "titleImageId");
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getPagedCriteria(Pageable pageable) {
        return setPageToCriteria(getCriteria(), pageable);
    }
}