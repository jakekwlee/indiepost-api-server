package com.indiepost.service;

import com.indiepost.model.Post;
import com.indiepost.model.legacy.LegacyPost;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyPostService {
    LegacyPost save(Post post);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);
}
