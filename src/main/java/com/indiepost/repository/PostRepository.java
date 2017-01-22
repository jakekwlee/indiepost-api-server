package com.indiepost.repository;

import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface PostRepository {

    Post findById(Long id);

    Long count();

    Long count(PostQuery query);

    List<PostSummaryDto> find(Pageable pageable);

    List<PostSummaryDto> findByQuery(PostQuery query, Pageable pageable);

    List<PostSummaryDto> findByStatus(PostEnum.Status status, Pageable pageable);

    List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable);
}