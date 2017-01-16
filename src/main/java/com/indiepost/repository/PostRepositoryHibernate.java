package com.indiepost.repository;

import com.github.fluent.hibernate.request.aliases.Aliases;
import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.dto.request.PostQuery;
import com.indiepost.repository.helper.CriteriaHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryHibernate implements PostRepository {

    private final String[] HOME_PROJECTION = {
            "id",
            "title",
            "excerpt",
            "publishedAt",
            "displayName",
            "commentsCount",
            "likesCount",
            "featuredImage",
            "category"
    };

    private final CriteriaHelper criteriaHelper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PostRepositoryHibernate(CriteriaHelper criteriaHelper) {
        this.criteriaHelper = criteriaHelper;
    }

    @Override
    public Post findById(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
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
    public Long count(PostQuery query) {
        Conjunction conjunction = Restrictions.conjunction();
        criteriaHelper.buildConjunction(query, conjunction);
        return (Long) getCriteria().add(conjunction).setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Post> find(Pageable pageable) {
        return find(null, pageable);
    }

    @Override
    public List<Post> find(PostQuery query, Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        getAliases().addToCriteria(criteria);
        criteria.setProjection(criteriaHelper.buildProjectionList(HOME_PROJECTION));
        Conjunction conjunction = Restrictions.conjunction();

        if (query != null) {
            criteriaHelper.buildConjunction(query, conjunction);
        }

        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }

        criteria.setResultTransformer(new FluentHibernateResultTransformer(Post.class));
        return criteria.list();
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        getAliases().addToCriteria(criteria);
        criteria.setProjection(criteriaHelper.buildProjectionList(HOME_PROJECTION));

        Criterion restrictions = Restrictions.eq("status", status);
        criteria.add(restrictions);
        criteria.setResultTransformer(new FluentHibernateResultTransformer(Post.class));
        return criteria.list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getPagedCriteria(Pageable pageable) {
        return criteriaHelper.setPageToCriteria(getCriteria(), pageable);
    }

    private Aliases getAliases() {
        return Aliases.create()
                .add("category", "c", JoinType.INNER);
    }
}