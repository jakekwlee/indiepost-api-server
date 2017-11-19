package com.indiepost.mapper;

import com.indiepost.dto.ContributorDto;
import com.indiepost.model.Contributor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContributorMapper {
    public static ContributorDto contributorToContributorDto(Contributor contributor) {
        ContributorDto dto = new ContributorDto();
        BeanUtils.copyProperties(contributor, dto);
        return dto;
    }

    public static List<ContributorDto> contributorsToContributorDtos(List<Contributor> contributors) {
        if (contributors == null) {
            return new ArrayList<>();
        }
        return contributors.stream()
                .map(contributor -> contributorToContributorDto(contributor))
                .collect(Collectors.toList());
    }

    public static Contributor contributorDtoToContributor(ContributorDto dto) {
        Contributor contributor = new Contributor();
        BeanUtils.copyProperties(dto, contributor);
        return contributor;
    }
}
