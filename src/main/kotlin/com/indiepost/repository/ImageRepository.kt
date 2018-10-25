package com.indiepost.repository

import com.indiepost.model.ImageSet
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 8/17/16.
 */
interface ImageRepository {

    fun save(imageSet: ImageSet)

    fun findById(id: Long): ImageSet?

    fun findByPrefix(prefix: String): ImageSet?

    fun findByIdsIn(ids: List<Long>): List<ImageSet>

    fun findAll(pageable: Pageable): List<ImageSet>

    fun findByPrefixes(prefixes: Set<String>): List<ImageSet>

    fun delete(imageSet: ImageSet)

    fun deleteById(id: Long?)

    fun count(): Long?
}
