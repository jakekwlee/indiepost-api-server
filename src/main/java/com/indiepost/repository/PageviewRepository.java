package com.indiepost.repository;

import com.indiepost.model.Pageview;

/**
 * Created by jake on 17. 4. 17.
 */
public interface PageviewRepository {

    Long save(Pageview pageview);

    void delete(Pageview pageview);

    Pageview findById(Long id);

    void update(Pageview pageview);
}
