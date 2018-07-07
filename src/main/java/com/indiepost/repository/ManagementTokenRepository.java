package com.indiepost.repository;

import com.indiepost.model.ManagementToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagementTokenRepository extends CrudRepository<ManagementToken, Integer> {
}
