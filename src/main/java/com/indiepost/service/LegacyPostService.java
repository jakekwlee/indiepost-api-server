package com.indiepost.service;

import com.indiepost.model.Post;
import com.indiepost.model.legacy.Contentlist;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyPostService {
    Contentlist save(Post post);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);
}
