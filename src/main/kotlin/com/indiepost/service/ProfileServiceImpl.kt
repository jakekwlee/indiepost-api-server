package com.indiepost.service

import com.indiepost.dto.ProfileDto
import com.indiepost.enums.Types
import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.mapper.createDtoWithPrivacy
import com.indiepost.mapper.createEntity
import com.indiepost.mapper.merge
import com.indiepost.model.Profile
import com.indiepost.repository.ProfileRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.inject.Inject
import javax.transaction.Transactional

@Service
@Transactional
open class ProfileServiceImpl @Inject constructor(
        private val profileRepository: ProfileRepository) : ProfileService {

    override fun findOne(id: Long): ProfileDto? {
        return profileRepository.findById(id)?.createDtoWithPrivacy()
    }

    override fun save(dto: ProfileDto): Long? {
        val profile: Profile?
        val postId = dto.id
        if (postId == null) {
            profile = dto.createEntity()
        } else {
            profile = profileRepository.findById(postId)
            if (profile != null) {
                profile.merge(dto)
            } else {
                throw ResourceNotFoundException("No Profile with this id: $postId")
            }
        }
        profileRepository.save(profile)
        return profile.id
    }

    override fun findOneBySlug(slug: String): ProfileDto? {
        return profileRepository.findBySlug(slug)?.createDtoWithPrivacy()
    }

    override fun deleteById(id: Long): Long? {
        profileRepository.deleteById(id)
        return id
    }

    override fun count(type: Types.ProfileType): Int {
        val ret = profileRepository.countAllByProfileType(type)
        return ret.toInt()
    }

    override fun count(): Int {
        return profileRepository.count().toInt()
    }

    override fun find(type: Types.ProfileType, pageable: Pageable): Page<ProfileDto> {
        val count = count(type)
        if (count == 0) {
            return PageImpl(emptyList(), pageable, 0)
        }
        val page = profileRepository.findAllByProfileType(type, pageable)
        return profilePageToDtoPage(page)
    }

    override fun find(pageable: Pageable): Page<ProfileDto> {
        val count = count()
        if (count == 0) {
            return PageImpl(emptyList(), pageable, 0)
        }
        val page = profileRepository.findAll(PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.Direction.DESC, "id"))
        return profilePageToDtoPage(page)
    }

    private fun profilePageToDtoPage(page: Page<Profile>): Page<ProfileDto> {
        if (page.content.isEmpty()) {
            return PageImpl<ProfileDto>(emptyList(), page.pageable, page.totalElements)
        }
        val profileList = page.content
        val dtoList: List<ProfileDto> = profileList.stream()
                .map { p -> p.createDtoWithPrivacy() }
                .collect(Collectors.toList())
        return PageImpl<ProfileDto>(dtoList, page.pageable, page.totalElements)

    }
}
