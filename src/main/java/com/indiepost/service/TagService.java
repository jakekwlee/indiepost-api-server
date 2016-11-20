package com.indiepost.service;

import com.indiepost.model.Tag;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagService {
    void save(Tag tag);

    Tag findById(Long id);

    Tag findByName(String name);

    List<Tag> findAll();

    List<String> findAllToStringList();

    List<Tag> findAll(int page, int maxResults);

    void update(Tag tag);

    void delete(Tag tag);
}
