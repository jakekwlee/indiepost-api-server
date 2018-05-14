package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ContributorRepository {

    Long save(Contributor contributor);

    void delete(Contributor contributor);

    void deleteById(Long id);

    Optional<Contributor> findById(Long id);

    Long count();

    Page<Contributor> findAll(Pageable pageable);

    Page<Contributor> findAllByContributorType(Types.ContributorType contributorType, Pageable pageable);

    Page<Contributor> findAllByFullName(String fullName, Pageable pageable);

    Page<Contributor> findByIdIn(List<Long> ids, Pageable pageable);

    Page<Contributor> findByFullNameIn(List<String> contributorNames, Pageable pageable);

    Long countAllByContributorType(Types.ContributorType contributorType);

}
