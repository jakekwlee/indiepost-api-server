package com.indiepost.service;

import com.indiepost.dto.TagDto;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
public interface TagService {
    TagDto save(TagDto tagDto);

    TagDto findById(Long id);

    TagDto findByName(String name);

    List<String> findAllToStringList();

    List<TagDto> findAll(int page, int maxResults);

    List<TagDto> findByIds(List<Long> ids);

    void update(TagDto tagDto);

    void deleteById(Long id);

    List<TagDto> findByNameIn(List<String> tagNames);

    List<TagDto> findByNameLike(String tagName);
}
