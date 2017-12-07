package com.indiepost.repository;

import com.indiepost.model.analytics.Pageview;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jake on 17. 4. 17.
 */
public interface PageviewRepository extends CrudRepository<Pageview, Long> {

}
