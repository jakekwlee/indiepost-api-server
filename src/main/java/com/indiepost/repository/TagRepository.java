package com.indiepost.repository;

import com.indiepost.model.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagRepository {

    Tag findOne(Long id);

    Tag findOneByName(String name);

    Long save(Tag tag);

    void delete(Tag tag);

    void deleteById(Long id);

    List<Tag> findByIdIn(List<Long> ids);

    List<Tag> findAll(Pageable pageable);
}
