package com.indiepost.service;

import com.indiepost.model.*;
import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.request.PostQuery;
import com.indiepost.dto.response.AdminPostResponseDto;
import com.indiepost.dto.response.AdminPostTableDto;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
public interface AdminPostService {
    Long save(Post post);

    Post findById(Long id);

    void update(Post post);

    void delete(Post post);

    void deleteById(Long id);

    List<Post> find(int page, int maxResults, boolean isDesc);

    List<Post> find(PostQuery query, int page, int maxResults, boolean isDesc);

    Long count();

    Long count(PostQuery query);

    AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto);

    void updateAutosave(Long id, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto);

    AdminPostResponseDto getPostResponse(Long id);

    List<AdminPostTableDto> getAdminPostTableDtoList(int page, int maxResults, boolean isDesc);

    List<AdminPostTableDto> getLastUpdated(Date dateFrom);

    List<String> findAllDisplayNames();

    void publishPosts();
}
