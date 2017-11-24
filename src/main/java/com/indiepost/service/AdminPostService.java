package com.indiepost.service;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostSearch;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
public interface AdminPostService {

    AdminPostResponseDto findOne(Long id);

    void deleteById(Long id);

    List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<AdminPostSummaryDto> search(PostSearch search, int page, int maxResults, boolean isDesc);

    Long count();

    Long count(PostSearch search);

    AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto);

    void updateAutosave(Long postId, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto);

    List<AdminPostSummaryDto> findLastUpdated(LocalDateTime dateFrom);

    List<String> findAllBylineNames();

}
