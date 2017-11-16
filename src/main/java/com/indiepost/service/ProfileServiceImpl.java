package com.indiepost.service;

import com.indiepost.dto.ProfileDto;
import com.indiepost.model.Profile;
import com.indiepost.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.indiepost.mapper.ProfileMapper.*;

@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileDto findById(Long id) {
        Profile profile = profileRepository.findOne(id);
        return profile != null ?
                profileToProfileDto(profile) :
                null;
    }

    @Override
    public List<ProfileDto> findAll() {
        List<Profile> profiles = profileRepository.findAllByOrderByCreatedAtAsc();
        return profilesToProfileDtos(profiles);
    }

    @Override
    public List<ProfileDto> findByIds(List<Long> ids) {
        List<Profile> profiles = profileRepository.findByIdIn(ids);
        return profilesToProfileDtos(profiles);
    }

    @Override
    public void delete(ProfileDto dto) {
        deleteById(dto.getId());
    }

    @Override
    public void deleteById(Long id) {
        profileRepository.delete(id);
    }

    @Override
    public void update(ProfileDto dto) {
        Profile profile = profileDtoToProfile(dto);
        profileRepository.save(profile);
    }
}
