package com.indiepost.service;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
public interface StaticPageService {

    Long save(StaticPageDto staticPageDto);

    void update(StaticPageDto staticPageDto);

    StaticPageDto findById(Long id);

    StaticPageDto findBySlug(String slug);

    Page<StaticPageDto> find(Pageable pageable);

    Page<StaticPageDto> find(Types.PostStatus status, Pageable pageable);

    void delete(StaticPageDto staticPageDto);

    void deleteById(Long id);

    Long count();

    void bulkUpdateStatus(List<Long> ids, Types.PostStatus status);

    void bulkDeleteByIds(List<Long> ids);

    void emptyTrash();
}
