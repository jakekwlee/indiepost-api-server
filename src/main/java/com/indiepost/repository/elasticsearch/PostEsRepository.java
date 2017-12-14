package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostEsRepository {

    boolean testConnection();

    boolean createIndex();

    boolean deleteIndex();

    void rebuildIndices(List<PostEs> posts);

    List<PostEs> search(String text, String status, Pageable pageable);

    PostEs findById(Long id);

    void save(List<PostEs> posts);

    void save(PostEs post);

    void update(PostEs post);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);

    void delete(PostEs postEs);
}
