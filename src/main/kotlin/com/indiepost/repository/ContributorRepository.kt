package com.indiepost.repository

import com.indiepost.enums.Types
import com.indiepost.model.Contributor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContributorRepository {

    fun save(contributor: Contributor): Long?

    fun delete(contributor: Contributor)

    fun deleteById(id: Long)

    fun findById(id: Long): Contributor?

    fun count(): Long

    fun findAll(pageable: Pageable): Page<Contributor>

    fun findAllByContributorType(contributorType: Types.ContributorType, pageable: Pageable): Page<Contributor>

    fun findAllByFullName(fullName: String, pageable: Pageable): Page<Contributor>

    fun findByIdIn(ids: List<Long>, pageable: Pageable): Page<Contributor>

    fun findByFullNameIn(contributorNames: List<String>, pageable: Pageable): Page<Contributor>

    fun countAllByContributorType(contributorType: Types.ContributorType): Long

}
