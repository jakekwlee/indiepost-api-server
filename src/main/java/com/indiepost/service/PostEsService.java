package com.indiepost.service;

import com.indiepost.enums.Types;
import com.indiepost.model.elasticsearch.PostEs;

import java.util.List;

public interface PostEsService {
    List findAll();

    List<PostEs> search(String text, int page, int maxResults);

    List<PostEs> search(String text, Types.PostStatus status, int page, int maxResults);
}
