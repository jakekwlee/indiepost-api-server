package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContributorRepository extends CrudRepository<Contributor, Long> {
    List<Contributor> findAllByContributorType(Types.ContributorType contributorType, Pageable pageable);

    Long countAllByContributorType(Types.ContributorType contributorType);
}
