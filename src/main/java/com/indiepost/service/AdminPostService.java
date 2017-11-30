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

    AdminPostResponseDto createAutosave();

    AdminPostResponseDto createAutosave(Long postId);

    void update(AdminPostRequestDto adminPostRequestDto);

    void deleteById(Long id);

    List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<AdminPostSummaryDto> search(PostSearch search, int page, int maxResults, boolean isDesc);

    Long count();

    Long count(PostSearch search);

    List<AdminPostSummaryDto> findLastUpdated(LocalDateTime dateFrom);

    List<String> findAllBylineNames();

}
