package com.indiepost.repository;

import com.indiepost.model.legacy.Detaillist;

import java.util.List;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyDetailListRepository {
    Long save(Detaillist detaillist);

    void update(Detaillist detaillist);

    void delete(Detaillist detaillist);

    Detaillist findByNo(Long no);

    List<Detaillist> findByParent(Long parent);
}
