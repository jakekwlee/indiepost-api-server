package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ContributorService {

    ContributorDto findOne(Long id);

    Long save(ContributorDto contributor);

    void delete(ContributorDto contributor);

    void deleteById(Long id);

    int count(Types.ContributorType type);

    Page<ContributorDto> find(Types.ContributorType type, Pageable pageable);
}

