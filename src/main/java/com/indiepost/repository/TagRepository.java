package com.indiepost.repository;

import com.indiepost.model.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {

    Tag findOneByName(String name);

    List<Tag> findByIdIn(List<Long> ids);
}
