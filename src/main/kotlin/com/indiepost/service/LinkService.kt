package com.indiepost.service

import com.indiepost.dto.LinkBoxRequest
import com.indiepost.dto.LinkBoxResponse
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

    fun getLinkBox(linkBoxRequest: LinkBoxRequest): LinkBoxResponse

    fun dtoToLink(linkDto: LinkDto): Link

    fun linkToDto(link: Link): LinkDto
}
