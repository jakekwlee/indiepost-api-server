package com.indiepost.service.mapper;

import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;

public class ContributorMapper {
    public static ContributorDto toDto(Contributor contributor) {
        ContributorDto dto = new ContributorDto();
        dto.setId(contributor.getId());
        dto.setFullName(contributor.getFullName());
        dto.setEmail(contributor.getEmail());
        dto.setEmailVisible(contributor.isEmailVisible());
        dto.setSubEmail(contributor.getSubEmail());
        dto.setPhone(contributor.getPhone());
        dto.setPhoneVisible(contributor.isPhoneVisible());
        dto.setPicture(contributor.getPicture());
        dto.setPictureVisible(contributor.isPictureVisible());
        dto.setUrl(contributor.getUrl());
        dto.setTitle(contributor.getTitle());
        dto.setTitleVisible(contributor.isTitleVisible());
        dto.setDescription(contributor.getDescription());
        dto.setDescriptionVisible(contributor.isDescriptionVisible());
        dto.setContributorType(contributor.getContributorType().toString());
        dto.setDisplayType(contributor.getDisplayType().toString());
        dto.setLastUpdated(contributor.getLastUpdated());
        dto.setCreated(contributor.getCreated());
        dto.setEtc(contributor.getEtc());
        return dto;
    }

    public static Contributor toEntity(ContributorDto dto) {
        Contributor contributor = new Contributor();
        copy(dto, contributor);
        return contributor;
    }

    public static void copy(ContributorDto dto, Contributor contributor) {
        copy(dto, contributor, true);
    }

    public static void copy(ContributorDto dto, Contributor contributor, boolean copyId) {
        contributor.setFullName(dto.getFullName());
        contributor.setTitle(dto.getTitle());
        contributor.setTitleVisible(dto.isTitleVisible());
        contributor.setPicture(dto.getPicture());
        contributor.setPictureVisible(dto.isPictureVisible());
        contributor.setEmail(dto.getEmail());
        contributor.setSubEmail(dto.getSubEmail());
        contributor.setEmailVisible(dto.isEmailVisible());
        contributor.setDescription(dto.getDescription());
        contributor.setDescriptionVisible(dto.isDescriptionVisible());
        contributor.setContributorType(Types.ContributorType.valueOf(dto.getContributorType()));
        contributor.setDisplayType(Types.ContributorDisplayType.valueOf(dto.getDisplayType()));
        contributor.setEtc(dto.getEtc());
        contributor.setPhone(dto.getPhone());
        contributor.setPhoneVisible(dto.isPhoneVisible());
        contributor.setUrl(dto.getUrl());
        contributor.setUrlVisible(dto.isUrlVisible());
    }
}
