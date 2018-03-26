package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContributorRepository extends CrudRepository<Contributor, Long> {

    Page<Contributor> findAll(Pageable pageable);

    Page<Contributor> findAllByContributorType(Types.ContributorType contributorType, Pageable pageable);

    Page<Contributor> findAllByFullName(String fullName, Pageable pageable);

    Page<Contributor> findByIdIn(List<Long> ids, Pageable pageable);

    Page<Contributor> findByFullNameIn(List<String> contributorNames, Pageable pageable);

    Long countAllByContributorType(Types.ContributorType contributorType);

    void deleteAllByIdGreaterThan(Long id);
}
