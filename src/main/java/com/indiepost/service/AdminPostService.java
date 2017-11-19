package com.indiepost.service;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
public interface AdminPostService {

    AdminPostResponseDto findOne(Long id);

    void deleteById(Long id);

    List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<AdminPostSummaryDto> find(PostQuery query);

    Long count();

    Long count(PostQuery query);

    AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto);

    void updateAutosave(Long postId, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto);

    List<AdminPostSummaryDto> getLastUpdated(LocalDateTime dateFrom);

    List<String> findAllDisplayNames();

}
