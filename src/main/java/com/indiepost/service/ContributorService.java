package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ContributorService {

    ContributorDto findOne(Long id);

    ContributorDto save(ContributorDto contributor);

    Long delete(ContributorDto contributor);

    Long deleteById(Long id);

    int count(Types.ContributorType type);

    int count();

    Page<ContributorDto> find(Types.ContributorType type, Pageable pageable);

    Page<ContributorDto> find(Pageable pageable);
}

