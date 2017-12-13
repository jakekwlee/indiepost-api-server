package com.indiepost.repository;

import com.indiepost.model.Metadata;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jake on 10/29/17.
 */
@Repository
public interface MetadataRepository extends CrudRepository<Metadata, Long> {

}
