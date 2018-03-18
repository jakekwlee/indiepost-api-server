package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface ContributorRepository extends CrudRepository<Contributor, Long> {

    Page<Contributor> findAll(Pageable pageable);

    Page<Contributor> findAllByContributorType(Types.ContributorType contributorType, Pageable pageable);

    Page<Contributor> findAllByFullName(String fullName, Pageable pageable);

    Long countAllByContributorType(Types.ContributorType contributorType);

    void deleteAllByIdGreaterThan(Long id);
}
