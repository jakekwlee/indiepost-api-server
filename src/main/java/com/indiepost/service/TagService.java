package com.indiepost.service;

import com.indiepost.model.Tag;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagService {
    void save(Tag tag);

    Tag findById(int id);

    Tag findByName(String name);

    List<Tag> findAll();

    String[] findAllToStringArray();

    List<Tag> findAll(int page, int maxResults);

    void update(Tag tag);

    void delete(Tag tag);
}