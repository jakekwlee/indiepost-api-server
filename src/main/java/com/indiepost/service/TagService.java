package com.indiepost.service;

import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagService {
    TagDto save(Tag tag);

    TagDto findById(Long id);

    TagDto findByName(String name);

    List<String> findAllToStringList();

    List<TagDto> findAll(int page, int maxResults);

    List<TagDto> findByIds(List<Long> ids);

    void update(TagDto tag);

    void deleteById(Long id);
}
