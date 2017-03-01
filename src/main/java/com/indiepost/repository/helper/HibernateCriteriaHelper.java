package com.indiepost.repository.helper;

import com.indiepost.dto.PostQuery;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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

    public void buildConjunction(PostQuery query, Conjunction conjunction) {
        if (query.getAuthorId() != null) {
            conjunction.add(Restrictions.eq("authorId", query.getAuthorId()));
        }
        if (query.getEditorId() != null) {
            conjunction.add(Restrictions.eq("editorId", query.getEditorId()));
        }
        if (query.getCategoryId() != null) {
            conjunction.add(Restrictions.eq("categoryId", query.getCategoryId()));
        }
        if (StringUtils.isNotEmpty(query.getCategorySlug())) {
            conjunction.add(Restrictions.ilike("category.slug", query.getCategorySlug()));
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
        if (query.isSplash()) {
            conjunction.add(Restrictions.eq("splash", query.isSplash()));
        }
        if (query.isFeatured()) {
            conjunction.add(Restrictions.eq("featured", query.isFeatured()));
        }
        if (query.isPicked()) {
            conjunction.add(Restrictions.eq("picked", query.isPicked()));
        }
    }
}
