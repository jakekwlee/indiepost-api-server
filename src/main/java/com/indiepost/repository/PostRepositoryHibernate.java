package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import com.indiepost.repository.helper.CriteriaHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryHibernate implements PostRepository {

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
    public Post findByLegacyId(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("legacyPostId", id))
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
        criteriaHelper.buildConjunction(query, conjunction);
        return (Long) getCriteria().add(conjunction).setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<PostSummaryDto> find(Pageable pageable) {
        return findByQuery(new PostQuery(), pageable);
    }


    @Override
    public List<PostSummaryDto> findByQuery(PostQuery query, Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        ProjectionList projectionList = this.getProjectionList();
        if (query != null && StringUtils.isNotEmpty(query.getCategorySlug())) {
            criteria.createAlias("category", "category");
            projectionList.add(Property.forName("category.slug"), "categoryName");
        }
        criteria.setProjection(projectionList);
        Conjunction conjunction = Restrictions.conjunction();

        if (query != null) {
            criteriaHelper.buildConjunction(query, conjunction);
        }
        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }
        criteria.setResultTransformer(new FluentHibernateResultTransformer(PostSummaryDto.class));

        return criteria.list();
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable) {
        PostQuery query = new PostQuery();
        if (categoryId != 0L) {
            query.setCategoryId(categoryId);
        }
        return this.findByQuery(query, pageable);
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        ProjectionList projectionList = getProjectionList()
                .add(Property.forName("legacyPostId"), "legacyPostId");
        Criteria criteria = getCriteria();
        criteria.setProjection(projectionList);
        criteria.add(Restrictions.in("id", ids));
        criteria.setResultTransformer(new FluentHibernateResultTransformer(PostSummaryDto.class));
        return criteria.list();
    }

    @Override
    public List<PostSummaryDto> findByStatus(PostStatus status, Pageable pageable) {
        PostQuery query = new PostQuery();
        query.setStatus(status);
        return this.findByQuery(query, pageable);
    }

    @Override
    public List<Post> search(String text, Pageable pageable) {
        Criteria criteria = getSession().createCriteria(Post.class);
        criteria.add(Restrictions.eq("status", PostStatus.PUBLISH))
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize());

        Sort sort = new Sort(
                SortField.FIELD_SCORE,
                new SortField("id_sortable", SortField.Type.LONG, true));

        FullTextQuery fullTextQuery = makeKeywordQuery(text);

        @SuppressWarnings("unchecked")
        List<Post> results = fullTextQuery
                .setCriteriaQuery(criteria)
                .setSort(sort)
                .getResultList();

        return results;
    }

    private FullTextQuery makePhraseQuery(String text) {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = getQueryBuilder();

        Query luceneQuery = queryBuilder
                .bool()
                .should(queryBuilder
                        .phrase()
                        .withSlop(1)
                        .onField("title")
                        .sentence(text)
                        .createQuery())
                .should(
                        queryBuilder
                                .phrase()
                                .withSlop(1)
                                .onField("excerpt")
                                .sentence(text)
                                .createQuery()
                )
                .should(
                        queryBuilder
                                .phrase()
                                .withSlop(1)
                                .onField("content")
                                .sentence(text)
                                .createQuery()
                ).createQuery();
        return fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);
    }

    private FullTextQuery makeKeywordQuery(String text) {
        FullTextEntityManager fullTextEntityManager =
                getFullTextEntityManager();

        QueryBuilder queryBuilder = getQueryBuilder();
        String[] splited = text.split("\\s+");

        Query luceneQuery = queryBuilder
                .keyword()
                .onFields("title", "excerpt", "content", "displayName", "tags.name")
                .matching(splited[0])
                .createQuery();

        if (splited.length > 1) {
            for (int i = 1; i < splited.length; ++i) {
                luceneQuery = queryBuilder
                        .bool()
                        .must(luceneQuery)
                        .must(queryBuilder
                                .keyword()
                                .onFields("title", "excerpt", "content", "displayName", "tags.name")
                                .matching(splited[i])
                                .createQuery()
                        )
                        .createQuery();
            }
        }

        return fullTextEntityManager.createFullTextQuery(luceneQuery, Post.class);
    }

    private FullTextEntityManager getFullTextEntityManager() {
        return Search.getFullTextEntityManager(entityManager);
    }

    private QueryBuilder getQueryBuilder() {
        return getFullTextEntityManager().getSearchFactory()
                .buildQueryBuilder().forEntity(Post.class).get();
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
        return criteriaHelper.setPageToCriteria(getCriteria(), pageable);
    }
}