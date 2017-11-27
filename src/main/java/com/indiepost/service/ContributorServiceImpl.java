package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.model.Contributor;
import com.indiepost.repository.ContributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.indiepost.mapper.ContributorMapper.*;

@Service
@Transactional
public class ContributorServiceImpl implements ContributorService {

    private final ContributorRepository contributorRepository;

    @Autowired
    public ContributorServiceImpl(ContributorRepository contributorRepository) {
        this.contributorRepository = contributorRepository;
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
    public void delete(ContributorDto dto) {
        deleteById(dto.getId());
    }

    @Override
    public void deleteById(Long id) {
        contributorRepository.deleteById(id);
    }

    @Override
    public void update(ContributorDto dto) {
        Contributor contributor = contributorDtoToContributor(dto);
        contributorRepository.save(contributor);
    }
}
