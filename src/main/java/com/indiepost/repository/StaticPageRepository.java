package com.indiepost.repository;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.StaticPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
public interface StaticPageRepository {

    Long save(StaticPage staticPage);

    StaticPage findById(Long id);

    void update(StaticPage staticPage);

    void delete(StaticPage staticPage);

    Page<StaticPageDto> find(Pageable pageable);

    Page<StaticPageDto> find(Pageable pageable, Types.PostStatus pageStatus);

    Long count();

    Long count(Types.PostStatus pageStatus);

    StaticPage findBySlug(String slug);

    void bulkUpdateStatusByIds(List<Long> ids, Types.PostStatus status);

    void bulkDeleteByIds(List<Long> ids);

    void bulkDeleteByStatus(Types.PostStatus status);

    boolean isExists(Long id);
}
