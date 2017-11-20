package com.indiepost.repository;

import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Post findByLegacyId(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("legacyPostId", id))
                .uniqueResult();
    }

    @Override
    public Long findIdByLegacyId(Long legacyId) {
        Projection projection = Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("legacyPostId"), "legacyPostId");

        Criteria criteria = getCriteria()
                .setProjection(projection)
                .add(Restrictions.eq("legacyPostId", legacyId))
                .setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);

        Map post = (HashMap) criteria.uniqueResult();
        return (Long) post.get("id");
    }

    @Override
    public Long count() {
        return (Long) getCriteria()
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(PostQuery query) {
        Conjunction conjunction = buildConjunction(query);
        return (Long) getCriteria()
                .add(conjunction)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Post> find(Pageable pageable) {
        return findByQuery(new PostQuery(), pageable);
    }


    @Override
    public List<Post> findByQuery(PostQuery query, Pageable pageable) {
        Conjunction conjunction = buildConjunction(query);

        // TODO
        Criteria criteria = this.getPagedCriteria(pageable);
        criteria.createAlias("category", "category");
        criteria.createAlias("titleImage", "titleImage", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("titleImage.images", "titleImage.images");
        ProjectionList projection = this.getSummaryProjection();
        criteria.setProjection(projection);

        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }
        return criteria.list();
    }

    @Override
    public List<Post> findByCategoryId(Long categoryId, Pageable pageable) {
        PostQuery query = new PostQuery();
        query.setCategoryId(categoryId);
        return this.findByQuery(query, pageable);
    }

    @Override
    public List<Post> findByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        Criteria criteria = getCriteria();
        ProjectionList projection = this.getSummaryProjection();
        criteria.setProjection(projection);
        criteria.add(Restrictions.in("id", ids));
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<Post> findByStatus(PostStatus status, Pageable pageable) {
        PostQuery query = new PostQuery();
        query.setStatus(status);
        return this.findByQuery(query, pageable);
    }

    @Override
    public List<Post> findScheduledPosts() {
        return getCriteria()
                .setProjection(getSummaryProjection())
                .add(Restrictions.eq("status", PostStatus.FUTURE))
                .addOrder(Order.asc("publishedAt"))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public List<Post> search(String text, Pageable pageable) {
        return null;
    }

    private ProjectionList getSummaryProjection() {
        return Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("legacyPostId"), "legacyPostId")
                .add(Property.forName("title"), "title")
                .add(Property.forName("excerpt"), "excerpt")
                .add(Property.forName("bylineName"), "bylineName")
                .add(Property.forName("category"), "category")
                .add(Property.forName("category.slug"), "category.slug")
                .add(Property.forName("publishedAt"), "publishedAt")
                .add(Property.forName("bookmarkCount"), "bookmarkCount")
                .add(Property.forName("featured"), "featured")
                .add(Property.forName("picked"), "picked")
                .add(Property.forName("splash"), "splash")
                .add(Property.forName("titleImage"), "titleImage")
                .add(Property.forName("titleImage.images"), "titleImage.images");
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