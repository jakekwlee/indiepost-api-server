package com.indiepost.repository;

import com.indiepost.model.analytics.Pageview;

/**
 * Created by jake on 17. 4. 17.
 */
public interface PageviewRepository {
    void save(Pageview pageview);
}
