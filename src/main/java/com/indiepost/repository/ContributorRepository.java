package com.indiepost.repository;

import com.indiepost.model.Contributor;

import java.util.List;

public interface ContributorRepository {
    Contributor findOne(Long id);

    Long save(Contributor contributor);

    void delete(Contributor contributor);

    void deleteById(Long id);

    List<Contributor> findByIdIn(List<Long> ids);

    List<Contributor> findAllByOrderByCreatedAtAsc();

    List<Contributor> findAllByOrderByCreatedAtDesc();
}
