package com.indiepost.repository;

import com.indiepost.model.legacy.LegacyPost;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyPostRepository {
    Long save(LegacyPost legacyPost);

    void update(LegacyPost legacyPost);

    void delete(LegacyPost legacyPost);

    LegacyPost findByNo(Long no);
}
