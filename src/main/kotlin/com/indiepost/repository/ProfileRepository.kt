package com.indiepost.repository

import com.indiepost.enums.Types
import com.indiepost.model.Profile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProfileRepository {

    fun save(profile: Profile): Long?

    fun delete(profile: Profile)

    fun deleteById(id: Long)

    fun findById(id: Long): Profile?

    fun findBySlug(slug: String): Profile?

    fun count(): Long

    fun findAll(pageable: Pageable): Page<Profile>

    fun findAllByProfileType(profileType: Types.ProfileType, pageable: Pageable): Page<Profile>

    fun findAllByFullName(fullName: String, pageable: Pageable): Page<Profile>

    fun findByIdIn(ids: List<Long>, pageable: Pageable): Page<Profile>

    fun findByFullNameIn(profileNames: List<String>, pageable: Pageable): Page<Profile>

    fun countAllByProfileType(profileType: Types.ProfileType): Long

}
