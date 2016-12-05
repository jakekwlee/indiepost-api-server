package com.indiepost.repository;

import com.indiepost.model.legacy.Contentlist;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyContentListRepository {
    Long save(Contentlist contentlist);

    void saveOrUpdate(Contentlist contentlist);

    void update(Contentlist contentlist);

    void delete(Contentlist contentlist);

    Contentlist findByNo(Long no);
}
