package com.indiepost.repository;

import com.indiepost.model.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagRepository {

    void save(Tag tag);

    Tag findByTagName(String name);

    Tag findById(Long id);

    List<Tag> findAll();

    List<Tag> findAll(Pageable pageable);

    List<Tag> findByNameIn(List<String> tagNames);

    void update(Tag tag);

    void delete(Tag tag);
}
