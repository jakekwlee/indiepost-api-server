package com.indiepost.service

import com.indiepost.dto.LinkMetadataBookResponse
import com.indiepost.dto.LinkMetadataFlimResponse
import com.indiepost.dto.LinkMetadataResponse
import com.indiepost.dto.analytics.LinkDto
import com.indiepost.model.analytics.Link

/**
 * Created by jake on 8/10/17.
 */
interface LinkService {

    fun save(linkDto: LinkDto): LinkDto

    fun update(linkDto: LinkDto)

    fun deleteById(id: Long)

    fun findById(id: Long): LinkDto?

    fun findByUid(uid: String): LinkDto?

    fun findAll(): List<LinkDto>

    fun dtoToLink(linkDto: LinkDto): Link

    fun linkToDto(link: Link): LinkDto

    fun searchMovies(text: String, limit: Int): List<LinkMetadataFlimResponse>

    fun searchBooks(text: String, size: Int): List<LinkMetadataBookResponse>

    fun getFromUrl(url: String): List<LinkMetadataResponse>
}
