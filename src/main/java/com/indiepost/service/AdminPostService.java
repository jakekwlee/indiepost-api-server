package com.indiepost.service;

import com.indiepost.dto.CreateResponse;
import com.indiepost.dto.DeleteResponse;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
import com.indiepost.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
public interface AdminPostService {

    AdminPostResponseDto findOne(Long id);

    CreateResponse createDraft(AdminPostRequestDto dto);

    CreateResponse createAutosave(AdminPostRequestDto requestDto);

    void update(AdminPostRequestDto postUpdateDto);

    void updateAutosave(AdminPostRequestDto requestDto);

    DeleteResponse deleteById(Long id);

    DeleteResponse delete(Post post);

    Page<AdminPostSummaryDto> find(Types.PostStatus status, Pageable pageable);

    Page<AdminPostSummaryDto> fullTextSearch(String text, Types.PostStatus status,
                                             Pageable pageable);

    Long count();

    Long count(PostQuery query);

    List<String> findAllBylineNames();

    void bulkDeleteByStatus(Types.PostStatus status);

    void bulkDeleteByIds(List<Long> ids);

    void bulkStatusUpdate(List<Long> ids, Types.PostStatus changeTo);

}
