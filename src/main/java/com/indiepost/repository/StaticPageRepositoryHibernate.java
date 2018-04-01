package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.StaticPage;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class StaticPageRepositoryHibernate implements StaticPageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(StaticPage staticPage) {
        return (Long) getSession().save(staticPage);
    }

    @Override
    public StaticPage findById(Long id) {
        return entityManager.find(StaticPage.class, id);
    }

    @Override
    public void update(StaticPage staticPage) {
        getSession().update(staticPage);
    }

    @Override
    public void delete(StaticPage staticPage) {
        getSession().delete(staticPage);
    }

    @Override
    public Page<StaticPageDto> find(Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        setProjectionForDto(criteria);
        List<StaticPageDto> staticPageDtoList = criteria.list();
        Long count = count();
        return new PageImpl<>(staticPageDtoList, pageable, count);
    }

    @Override
    public Page<StaticPageDto> find(Pageable pageable, Types.PostStatus pageStatus) {
        Criteria criteria = getPagedCriteria(pageable);
        criteria.add(Restrictions.eq("status", pageStatus));
        setProjectionForDto(criteria);
        List<StaticPageDto> staticPageDtoList = criteria.list();
        Long count = count(pageStatus);
        return new PageImpl<>(staticPageDtoList, pageable, count);
    }

    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(Types.PostStatus pageStatus) {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .add(Restrictions.eq("status", pageStatus))
                .uniqueResult();
    }

    @Override
    public StaticPage findBySlug(String slug) {
        return (StaticPage) getCriteria()
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
                .setResultTransformer(new FluentHibernateResultTransformer(StaticPageDto.class));
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(StaticPage.class);
    }

    private Criteria getPagedCriteria(Pageable pageable) {
        return setPageToCriteria(getCriteria(), pageable);
    }
}
