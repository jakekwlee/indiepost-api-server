package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostEsRepository {

    boolean testConnection();

    boolean indexExist();

    boolean createIndex();

    boolean deleteIndex();

    void buildIndex(List<PostEs> posts);

    void rebuildIndices(List<PostEs> posts);

    List<PostEs> search(String text, String status, Pageable pageable);

    PostEs findById(Long id);

    void index(PostEs post);

    void bulkIndex(List<PostEs> posts);

    void update(PostEs post);

    void deleteById(Long id);

    void delete(PostEs postEs);

    void bulkDelete(List<Long> ids);
}
