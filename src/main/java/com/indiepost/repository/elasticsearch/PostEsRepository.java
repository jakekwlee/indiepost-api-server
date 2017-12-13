package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostEsRepository {
    List<PostEs> search(String text, String status, Pageable pageable);
}
