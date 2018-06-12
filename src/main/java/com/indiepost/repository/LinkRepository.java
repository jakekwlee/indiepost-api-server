package com.indiepost.repository;

import com.indiepost.model.analytics.Link;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jake on 8/9/17.
 */
public interface LinkRepository extends CrudRepository<Link, Long> {

    Link findByUid(String uid);
}
