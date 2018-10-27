package com.indiepost.service

import com.indiepost.dto.ContributorDto
import com.indiepost.enums.Types
import com.indiepost.mapper.ContributorMapper.*
import com.indiepost.model.Contributor
import com.indiepost.repository.ContributorRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
@Transactional
class ContributorServiceImpl @Inject constructor(
        private val contributorRepository: ContributorRepository) : ContributorService {

    override fun findOne(id: Long): ContributorDto? {
        val contributor = contributorRepository.findById(id)
        return if (contributor != null) {
            toDto(contributor)
        } else null
    }

    override fun save(dto: ContributorDto): ContributorDto {
        val now = LocalDateTime.now()
        val contributor: Contributor?
        // TODO remove verbose code
        if (dto.id == null) {
            contributor = toEntity(dto)
            contributor!!.created = now
        } else {
            contributor = contributorRepository.findById(dto.id!!)
            if (contributor != null) {
                copy(dto, contributor)
            } else {
                throw EntityNotFoundException("No Contributor with this id: " + dto.id!!.toString())
            }
        }
        contributor.lastUpdated = now
        contributorRepository.save(contributor)
        return toDto(contributor)
    }

    override fun delete(dto: ContributorDto): Long? {
        contributorRepository.deleteById(dto.id!!)
        return dto.id
    }

    override fun deleteById(id: Long): Long? {
        contributorRepository.deleteById(id)
        return id
    }

    override fun count(type: Types.ContributorType): Int {
        val ret = contributorRepository.countAllByContributorType(type)
        return ret.toInt()
    }

    override fun count(): Int {
        return contributorRepository.count().toInt()
    }

    override fun find(type: Types.ContributorType, pageable: Pageable): Page<ContributorDto> {
        val count = count(type)
        if (count == 0) {
            return PageImpl(ArrayList(), pageable, 0)
        }
        val contributorList = contributorRepository.findAllByContributorType(type, pageable)
        val dtoList = contributorList.content
                .stream()
                .map { contributor -> toDto(contributor) }
                .collect(Collectors.toList())
        return PageImpl(dtoList, pageable, count.toLong())

    }

    override fun find(pageable: Pageable): Page<ContributorDto> {
        val count = count()
        if (count == 0) {
            return PageImpl(ArrayList(), pageable, 0)
        }
        val contributorList = contributorRepository.findAll(PageRequest.of(
                pageable.pageNumber,
                pageable.pageSize,
                Sort.Direction.DESC, "id"))
        val dtoList = contributorList.content
                .stream()
                .map { contributor -> toDto(contributor) }
                .collect(Collectors.toList())
        return PageImpl(dtoList, pageable, count.toLong())
    }
}
