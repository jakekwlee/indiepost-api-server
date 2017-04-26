package com.indiepost.service;

import com.indiepost.dto.PostQuery;
import com.indiepost.dto.admin.AdminPostRequestDto;
import com.indiepost.dto.admin.AdminPostResponseDto;
import com.indiepost.dto.admin.AdminPostSummaryDto;
import com.indiepost.model.Post;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
public interface AdminPostService {
    Long save(Post post);

    Post findById(Long id);

    AdminPostResponseDto getDtoById(Long id);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);

    List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc);

    List<AdminPostSummaryDto> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc);

    Long count();

    Long count(PostQuery query);

    AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto);

    void updateAutosave(Long id, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto getPostResponse(Long id);

    List<AdminPostSummaryDto> getAdminPostTableDtoList(int page, int maxResults, boolean isDesc);

    List<AdminPostSummaryDto> getLastUpdated(Date dateFrom);

    List<String> findAllDisplayNames();

    void publishScheduledPosts();
}
