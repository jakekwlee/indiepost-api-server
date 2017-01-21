package com.indiepost.repository.helper;

import com.indiepost.dto.PostQuery;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.ProjectionList;
import org.springframework.data.domain.Pageable;

/**
 * Created by jake on 17. 1. 14.
 */
public interface CriteriaHelper {
    Criteria setPageToCriteria(Criteria criteria, Pageable pageable);

    ProjectionList buildProjectionList(String[] projectionStringArray);

    void buildConjunction(PostQuery query, Conjunction conjunction);
}
