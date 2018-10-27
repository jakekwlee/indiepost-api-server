package com.indiepost.service

import com.indiepost.dto.ContributorDto
import com.indiepost.enums.Types
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface ContributorService {

    fun findOne(id: Long): ContributorDto?

    fun save(dto: ContributorDto): ContributorDto

    fun delete(dto: ContributorDto): Long?

    fun deleteById(id: Long): Long?

    fun count(type: Types.ContributorType): Int

    fun count(): Int

    fun find(type: Types.ContributorType, pageable: Pageable): Page<ContributorDto>

    fun find(pageable: Pageable): Page<ContributorDto>
}

