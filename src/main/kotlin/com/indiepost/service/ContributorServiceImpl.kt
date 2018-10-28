package com.indiepost.service

import com.indiepost.dto.ContributorDto
import com.indiepost.enums.Types
import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.mapper.createDto
import com.indiepost.mapper.createEntity
import com.indiepost.mapper.merge
import com.indiepost.model.Contributor
import com.indiepost.repository.ContributorRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject
import javax.transaction.Transactional

@Service
@Transactional
class ContributorServiceImpl @Inject constructor(
        private val contributorRepository: ContributorRepository) : ContributorService {

    override fun findOne(id: Long): ContributorDto? {
        return contributorRepository.findById(id)?.createDto()
    }

    override fun save(dto: ContributorDto): ContributorDto {
        val contributor: Contributor?
        val postId = dto.id
        if (postId == null) {
            contributor = dto.createEntity()
        } else {
            contributor = contributorRepository.findById(postId)
            if (contributor != null) {
                contributor.merge(dto)
            } else {
                throw ResourceNotFoundException("No Contributor with this id: $postId")
            }
        }
        contributorRepository.save(contributor)
        return contributor.createDto()
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
                .map { contributor -> contributor.createDto() }
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
                .map { contributor -> contributor.createDto() }
                .collect(Collectors.toList())
        return PageImpl(dtoList, pageable, count.toLong())
    }
}
