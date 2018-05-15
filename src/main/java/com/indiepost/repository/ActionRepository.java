package com.indiepost.repository;

import com.indiepost.model.analytics.Action;

/**
 * Created by jake on 8/9/17.
 */
public interface ActionRepository {
    void save(Action action);
}
