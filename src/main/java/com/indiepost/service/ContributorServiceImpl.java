package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import com.indiepost.mapper.ContributorMapper;
import com.indiepost.model.Contributor;
import com.indiepost.repository.ContributorRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.indiepost.mapper.ContributorMapper.*;

@Service
@Transactional
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRepository contributorRepository;

    @Inject
    public ContributorServiceImpl(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
    }

    @Override
    public ContributorDto findOne(Long id) {
        Optional<Contributor> contributor = contributorRepository.findById(id);
        return contributor
                .map(c -> ContributorMapper.toDto(c))
                .orElse(null);
    }

    @Override
    public ContributorDto save(ContributorDto dto) {
        LocalDateTime now = LocalDateTime.now();
        Contributor contributor;
        // TODO remove verbose code
        if (dto.getId() == null) {
            contributor = toEntity(dto);
            contributor.setCreated(now);
        } else {
            Optional<Contributor> optionalContributor = contributorRepository.findById(dto.getId());
            if (optionalContributor.isPresent()) {
                contributor = optionalContributor.get();
                copy(dto, contributor);
            } else {
                optionalContributor.orElseThrow(EntityNotFoundException::new);
                return null;
            }
        }
        contributor.setLastUpdated(now);
        contributorRepository.save(contributor);
        return toDto(contributor);
    }

    @Override
    public Long delete(ContributorDto dto) {
        contributorRepository.deleteById(dto.getId());
        return dto.getId();
    }

    @Override
    public Long deleteById(Long id) {
        contributorRepository.deleteById(id);
        return id;
    }

    @Override
    public int count(Types.ContributorType type) {
        return contributorRepository.countAllByContributorType(type).intValue();
    }

    @Override
    public int count() {
        return new Long(contributorRepository.count()).intValue();
    }

    @Override
    public Page<ContributorDto> find(Types.ContributorType type, Pageable pageable) {
        int count = count(type);
        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        Page<Contributor> contributorList = contributorRepository.findAllByContributorType(type, pageable);
        List<ContributorDto> dtoList = contributorList.getContent()
                .stream()
                .map(contributor -> toDto(contributor))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, count);

    }

    @Override
    public Page<ContributorDto> find(Pageable pageable) {
        int count = count();
        if (count == 0) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        Page<Contributor> contributorList = contributorRepository.findAll(PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.Direction.DESC, "id"));
        List<ContributorDto> dtoList = contributorList.getContent()
                .stream()
                .map(contributor -> toDto(contributor))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, count);
    }
}
