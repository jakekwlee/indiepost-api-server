package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import com.indiepost.repository.ContributorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

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
    public ContributorDto save(ContributorDto contributorDto) {
        Contributor contributor = new Contributor();
        BeanUtils.copyProperties(contributorDto, contributor);
        contributor.setRole(
                Types.ContributorRole.valueOf(contributorDto.getRole())
        );
        contributorRepository.save(contributor);

        ContributorDto result = new ContributorDto();
        BeanUtils.copyProperties(contributor, result);
        result.setRole(contributor.getRole().toString());
        return result;
    }

    @Override
    public ContributorDto findById(Long id) {
        Contributor contributor = contributorRepository.findOne(id);
        return contributor != null ?
                contributorToContributorDto(contributor) :
                null;
    }

    @Override
    public List<ContributorDto> findAll() {
        List<Contributor> contributors = contributorRepository.findAllByOrderByCreatedAtAsc();
        return contributorsToContributorDtos(contributors);
    }

    @Override
    public List<ContributorDto> findByIds(List<Long> ids) {
        List<Contributor> contributors = contributorRepository.findByIdIn(ids);
        return contributorsToContributorDtos(contributors);
    }

    @Override
    public void update(ContributorDto dto) {
        Contributor contributor = contributorDtoToContributor(dto);
        contributorRepository.save(contributor);
    }

    @Override
    public void deleteById(Long id) {
        contributorRepository.deleteById(id);
    }

    @Override
    public List<ContributorDto> findByNameIn(List<String> contributorNames) {
        List<Contributor> contributors = contributorRepository.findByNameIn(contributorNames);
        return contributorsToContributorDtos(contributors);
    }
}
