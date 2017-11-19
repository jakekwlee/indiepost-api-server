package com.indiepost.repository.elasticsearch;

import com.indiepost.model.elasticsearch.PostEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEsRepository extends ElasticsearchCrudRepository<PostEs, Long> {
}
