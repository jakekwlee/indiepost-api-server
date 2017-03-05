package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.PageDto;
import com.indiepost.model.Page;
import com.indiepost.repository.helper.CriteriaHelper;
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
import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
@Repository
public class PageRepositoryHibernate implements PageRepository {

    private final CriteriaHelper criteriaHelper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PageRepositoryHibernate(CriteriaHelper criteriaHelper) {
        this.criteriaHelper = criteriaHelper;
    }

    @Override
    public Long save(Page page) {
        return (Long) getSession().save(page);
    }

    @Override
    public Page findById(Long id) {
        return entityManager.find(Page.class, id);
    }

    @Override
    public void update(Page page) {
        getSession().update(page);
    }

    @Override
    public void delete(Page page) {
        getSession().delete(page);
    }

    @Override
    public List<PageDto> find(Pageable pageable) {
        ProjectionList projectionList = Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("title"), "title")
                .add(Property.forName("slug"), "slug")
                .add(Property.forName("displayOrder"), "displayOrder")
                .add(Property.forName("createdAt"), "createdAt")
                .add(Property.forName("modifiedAt"), "modifiedAt")
                .add(Property.forName("type"), "type")
                .add(Property.forName("a.displayName"), "authorDisplayName");

        return getPagedCriteria(pageable)
                .createAlias("author", "a")
                .setProjection(projectionList)
                .setResultTransformer(new FluentHibernateResultTransformer(PageDto.class))
                .list();
    }

    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Page findBySlug(String slug) {
        return (Page) getCriteria()
                .add(Restrictions.eq("slug", slug))
                .uniqueResult();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Page.class);
    }

    private Criteria getPagedCriteria(Pageable pageable) {
        return criteriaHelper.setPageToCriteria(getCriteria(), pageable);
    }
}
