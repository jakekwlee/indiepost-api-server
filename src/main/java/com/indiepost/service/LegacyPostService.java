package com.indiepost.service;

import com.indiepost.model.Post;

/**
 * Created by jake on 11/22/16.
 */
public interface LegacyPostService {
    Long saveOrUpdate(Post post);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);
}
