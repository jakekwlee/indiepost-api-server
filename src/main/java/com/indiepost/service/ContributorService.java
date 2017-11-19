package com.indiepost.service;

import com.indiepost.dto.ContributorDto;

import java.util.List;

public interface ContributorService {
    ContributorDto findById(Long id);

    List<ContributorDto> findAll();

    List<ContributorDto> findByIds(List<Long> ids);

    void delete(ContributorDto dto);

    void deleteById(Long id);

    void update(ContributorDto dto);
}
