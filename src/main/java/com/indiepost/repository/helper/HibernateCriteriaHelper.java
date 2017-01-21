package com.indiepost.repository.helper;

import com.indiepost.dto.PostQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Created by jake on 8/17/16.
 */
@Component
public class HibernateCriteriaHelper implements CriteriaHelper {
    public Criteria setPageToCriteria(Criteria criteria, Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort != null) {
            for (Sort.Order order : sort) {
                if (order.getDirection().equals(Sort.Direction.ASC)) {
                    criteria.addOrder(Order.asc(order.getProperty()));
                } else {
                    criteria.addOrder(Order.desc(order.getProperty()));
                }
            }
        }
        criteria.setFirstResult(pageable.getOffset());
        criteria.setMaxResults(pageable.getPageSize());
        return criteria;
    }

    public ProjectionList buildProjectionList(String[] projectionStringArray) {
        ProjectionList projectionList = Projections.projectionList();
        for (String propertyName : projectionStringArray) {
            projectionList.add(Property.forName(propertyName), propertyName);
        }
        return projectionList;
    }

    public void buildConjunction(PostQuery query, Conjunction conjunction) {
        if (query.getAuthorId() != null) {
            conjunction.add(Restrictions.eq("author.id", query.getAuthorId()));
        }
        if (query.getEditorId() != null) {
            conjunction.add(Restrictions.eq("editor.id", query.getEditorId()));
        }
        if (query.getCategoryId() != null) {
            conjunction.add(Restrictions.eq("category.id", query.getCategoryId()));
        }
        if (StringUtils.isNotEmpty(query.getCategorySlug())) {
            conjunction.add(Restrictions.ilike("category.slug", query.getCategorySlug()));
        }
        if (StringUtils.isNotEmpty(query.getTitleContains())) {
            conjunction.add(Restrictions.ilike("title", query.getTitleContains()));
        }
        if (StringUtils.isNotEmpty(query.getContentContains())) {
            conjunction.add(Restrictions.ilike("content", query.getContentContains()));
        }
        if (StringUtils.isNotEmpty(query.getDisplayNameContains())) {
            conjunction.add(Restrictions.ilike("displayName", query.getContentContains()));
        }
        if (StringUtils.isNotEmpty(query.getTagNameContains())) {
            // TODO tag search
        }
        if (query.getStatus() != null) {
            conjunction.add(Restrictions.eq("status", query.getStatus()));
        }
        if (query.getType() != null) {
            conjunction.add(Restrictions.eq("postType", query.getType()));
        }
        if (query.getDateFrom() != null) {
            conjunction.add(Restrictions.ge("publishedAt", query.getDateFrom()));
        }
        if (query.getDateTo() != null) {
            conjunction.add(Restrictions.le("publishedAt", query.getDateTo()));
        }
    }
}
