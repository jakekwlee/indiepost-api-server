package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostEsRepositoryJest implements PostEsRepository {
    @Override
    public List<PostEs> search(String text, String status, Pageable pageable) {
        return null;
    }
}
