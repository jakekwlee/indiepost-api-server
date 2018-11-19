package com.indiepost.service

import com.indiepost.enums.Types
import com.indiepost.model.Profile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProfileService {
    fun findOne(id: Long): Profile?

    fun findOneBySlug(slug: String): Profile?

    fun save(fromClient: Profile): Profile

    fun delete(fromClient: Profile): Long?

    fun deleteById(id: Long): Long?

    fun count(type: Types.ProfileType): Int

    fun count(): Int

    fun find(type: Types.ProfileType, pageable: Pageable): Page<Profile>

    fun find(pageable: Pageable): Page<Profile>
}
