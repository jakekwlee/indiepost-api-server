package com.indiepost.repository;

import com.indiepost.model.legacy.LegacyPostContent;

import java.util.List;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyPostContentRepository {

    Long save(LegacyPostContent legacyPostContent);

    void update(LegacyPostContent legacyPostContent);

    void delete(LegacyPostContent legacyPostContent);

    LegacyPostContent findByNo(Long no);

    List<LegacyPostContent> findByParent(Long parent);
}
