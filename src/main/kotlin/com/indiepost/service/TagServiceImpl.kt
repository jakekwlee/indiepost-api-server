package com.indiepost.service

import com.indiepost.model.Tag
import com.indiepost.repository.TagRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
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

    override fun findAll(): List<Tag> {
        return tagRepository.findAll()
    }

    override fun findAllToStringList(): List<String> {
        val tags = findAll()
        val result = ArrayList<String>()
        for ((_, name) in tags) {
            result.add(name!!)
        }
        return result
    }

    override fun findAll(page: Int, maxResults: Int): List<Tag> {
        return tagRepository.findAll(PageRequest.of(page, maxResults, Sort.Direction.DESC, "id"))
    }

    override fun delete(tag: Tag) {
        tagRepository.delete(tag)
    }


}
