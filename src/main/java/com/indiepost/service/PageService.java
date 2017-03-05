package com.indiepost.service;

import com.indiepost.dto.PageDto;

import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
public interface PageService {

    Long save(PageDto pageDto);

    void update(PageDto pageDto);

    PageDto findById(Long id);

    PageDto findBySlug(String slug);

    List<PageDto> find(int page, int maxResults, boolean isDesc);

    void delete(PageDto pageDto);

    void deleteById(Long id);

    Long count();

}
