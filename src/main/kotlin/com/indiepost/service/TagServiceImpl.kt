package com.indiepost.service

import com.indiepost.dto.SelectedTagDto
import com.indiepost.dto.TagDto
import com.indiepost.enums.Types
import com.indiepost.mapper.toDto
import com.indiepost.model.Tag
import com.indiepost.repository.TagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors
import javax.inject.Inject

/**
 * Created by jake on 9/17/16.
 */
@Service
@Transactional
class TagServiceImpl @Inject constructor(
        private val tagRepository: TagRepository) : TagService {
    override fun save(tag: Tag) {
        tagRepository.save(tag)
    }

    override fun findById(id: Long): Tag? {
        return tagRepository.findById(id)
    }

    override fun findByName(name: String): Tag? {
        return tagRepository.findByTagName(name)
    }

    override fun find(): List<Tag> {
        return tagRepository.findAll()
    }

    override fun findAllToStringList(): List<String> {
        val tags = find()
        val result = ArrayList<String>()
        for ((_, name) in tags) {
            result.add(name!!)
        }
        return result
    }

    override fun find(pageable: Pageable): List<TagDto> {
        // TODO
        val tagList = tagRepository.findAll(
                PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.Direction.DESC, "id"))
        return tagList.stream().map { it.toDto() }.collect(Collectors.toList())
    }

    override fun delete(tag: Tag) {
        tagRepository.delete(tag)
    }

    override fun findSelected(): List<SelectedTagDto> {
        return tagRepository.findSelected()
    }

    override fun updateSelected(tags: List<String>) {
        tagRepository.updateSelected(tags)
    }
}
