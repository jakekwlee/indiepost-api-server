package com.indiepost.repository.helper;

import org.hibernate.Criteria;
import org.springframework.data.domain.Pageable;

/**
 * Created by jake on 8/17/16.
 */
public interface CriteriaMaker {
    Criteria getPagedCriteria(Criteria criteria, Pageable pageable);
}
