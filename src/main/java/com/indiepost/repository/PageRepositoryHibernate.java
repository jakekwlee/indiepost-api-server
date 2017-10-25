package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.PageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Page;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.setPageToCriteria;

/**
 * Created by jake on 17. 3. 5.
 */
@Repository
public class PageRepositoryHibernate implements PageRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
        Criteria criteria = getPagedCriteria(pageable);
        setProjectionForDto(criteria);
        return criteria.list();
    }

    @Override
    public List<PageDto> find(Pageable pageable, Types.PostStatus pageStatus) {
        Criteria criteria = getPagedCriteria(pageable);
        criteria.add(Restrictions.eq("status", pageStatus));
        setProjectionForDto(criteria);
        return criteria.list();
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

    private void setProjectionForDto(Criteria criteria) {
        criteria.createAlias("author", "a")
                .setProjection(
                        Projections.projectionList()
                                .add(Property.forName("id"), "id")
                                .add(Property.forName("title"), "title")
                                .add(Property.forName("slug"), "slug")
                                .add(Property.forName("createdAt"), "createdAt")
                                .add(Property.forName("modifiedAt"), "modifiedAt")
                                .add(Property.forName("displayOrder"), "displayOrder")
                                .add(Property.forName("type"), "type")
                                .add(Property.forName("status"), "status")
                                .add(Property.forName("a.displayName"), "authorDisplayName")
                )
                .setResultTransformer(new FluentHibernateResultTransformer(PageDto.class));
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Page.class);
    }

    private Criteria getPagedCriteria(Pageable pageable) {
        return setPageToCriteria(getCriteria(), pageable);
    }
}
