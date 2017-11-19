package com.indiepost.repository;

import com.indiepost.model.Contributor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributorRepository extends CrudRepository<Contributor, Long> {
    List<Contributor> findByIdIn(List<Long> ids);

    List<Contributor> findAllByOrderByCreatedAtAsc();

    List<Contributor> findAllByOrderByCreatedAtDesc();
}
