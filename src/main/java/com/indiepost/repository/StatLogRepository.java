package com.indiepost.repository;

import com.indiepost.model.StatLog;

/**
 * Created by jake on 17. 4. 9.
 */
public interface StatLogRepository {
    void save(StatLog statLog);

    void update(StatLog statLog);

    StatLog findById(Long id);
}
