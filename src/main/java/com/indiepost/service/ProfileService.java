package com.indiepost.service;

import com.indiepost.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    ProfileDto findById(Long id);

    List<ProfileDto> findAll();

    List<ProfileDto> findByIds(List<Long> ids);

    void delete(ProfileDto dto);

    void deleteById(Long id);

    void update(ProfileDto dto);
}
