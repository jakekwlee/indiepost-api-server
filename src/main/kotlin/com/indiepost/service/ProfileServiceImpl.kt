package com.indiepost.service

import com.indiepost.enums.Types
import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.mapper.merge
import com.indiepost.model.Profile
import com.indiepost.repository.ProfileRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@Service
@Transactional
class ProfileServiceImpl @Inject constructor(
        private val profileRepository: ProfileRepository) : ProfileService {

    override fun findOne(id: Long): Profile? {
        return profileRepository.findById(id)
    }

    override fun save(fromClient: Profile): Profile {
        val profile: Profile?
        val postId = fromClient.id
        if (postId == null) {
            profile = fromClient
        } else {
            profile = profileRepository.findById(postId)
            if (profile != null) {
                profile.merge(fromClient)
            } else {
                throw ResourceNotFoundException("No Profile with this id: $postId")
            }
        }
        profileRepository.save(profile)
        return profile
    }

    override fun findOneBySlug(slug: String): Profile? {
        return profileRepository.findBySlug(slug)
    }

    override fun delete(fromClient: Profile): Long? {
        profileRepository.deleteById(fromClient.id!!)
        return fromClient.id
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

    override fun find(type: Types.ProfileType, pageable: Pageable): Page<Profile> {
        val count = count(type)
        if (count == 0) {
            return PageImpl(ArrayList(), pageable, 0)
        }
        return profileRepository.findAllByProfileType(type, pageable)
    }

    override fun find(pageable: Pageable): Page<Profile> {
        val count = count()
        if (count == 0) {
            return PageImpl(ArrayList(), pageable, 0)
        }
        return profileRepository.findAll(PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.Direction.DESC, "id"))
    }
}
