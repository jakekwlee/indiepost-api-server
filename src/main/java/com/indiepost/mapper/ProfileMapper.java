package com.indiepost.mapper;

import com.indiepost.dto.ProfileDto;
import com.indiepost.model.Profile;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileMapper {
    public static ProfileDto profileToProfileDto(Profile profile) {
        ProfileDto dto = new ProfileDto();
        BeanUtils.copyProperties(profile, dto);
        return dto;
    }

    public static List<ProfileDto> profilesToProfileDtos(List<Profile> profiles) {
        if (profiles == null) {
            return new ArrayList<>();
        }
        return profiles.stream()
                .map(profile -> profileToProfileDto(profile))
                .collect(Collectors.toList());
    }

    public static Profile profileDtoToProfile(ProfileDto dto) {
        Profile profile = new Profile();
        BeanUtils.copyProperties(dto, profile);
        return profile;
    }
}
