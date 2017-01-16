package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.*;
import com.indiepost.dto.request.PostQuery;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
public interface PostService {

    Post findById(Long id);

    void update(Post post);

    Long count();

    Long count(PostQuery query);

    List<Post> find(int page, int maxResults, boolean isDesc);

    List<Post> find(PostQuery query, int page, int maxResults, boolean isDesc);

    List<Post> find(PostEnum.Status status, int page, int maxResults, boolean isDesc);
}