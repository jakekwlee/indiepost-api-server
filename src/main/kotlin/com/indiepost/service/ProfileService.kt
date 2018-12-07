package com.indiepost.service

import com.indiepost.dto.ProfileDto
import com.indiepost.dto.ProfileSummaryDto
import com.indiepost.enums.Types
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProfileService {
    fun findOne(id: Long): ProfileDto?

    fun findByIdIn(ids: List<Long>): List<ProfileDto>

    fun findOneBySlug(slug: String): ProfileDto?

    fun save(dto: ProfileDto): Long?

    fun deleteById(id: Long): Long?

    fun count(type: Types.ProfileType): Int

    fun count(): Int

    fun find(type: Types.ProfileType, pageable: Pageable): Page<ProfileDto>

    fun find(pageable: Pageable): Page<ProfileDto>

    fun getSummaryDtoList(): List<ProfileSummaryDto>
}
