package com.indiepost.service

import com.indiepost.dto.analytics.LinkDto
import com.indiepost.model.analytics.Link
import com.indiepost.repository.ClickRepository
import com.indiepost.repository.LinkRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.inject.Inject
import javax.transaction.Transactional

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
class LinkServiceImpl @Inject constructor(
        private val linkRepository: LinkRepository,
        private val clickRepository: ClickRepository) : LinkService {

    override fun save(linkDto: LinkDto): LinkDto {
        val link = dtoToLink(linkDto)
        linkRepository.save(link)
        return linkToDto(link)
    }

    override fun update(linkDto: LinkDto) {
        val link = dtoToLink(linkDto)
        linkRepository.save(link)
    }

    override fun deleteById(id: Long) {
        linkRepository.deleteById(id)
    }

    override fun findById(id: Long): LinkDto? {
        val optional = linkRepository.findById(id)
        if (optional.isPresent) {
            val link = optional.get()
            return linkToDto(link)
        } else {
            return null
        }
    }

    override fun findByUid(uid: String): LinkDto? {
        if (!isValidUid(uid)) {
            return null
        }
        val link = linkRepository.findByUid(uid)
        return linkToDto(link)
    }

    override fun findAll(): List<LinkDto> {
        val links = linkRepository.findAll() as List<Link>
        return links.stream()
                .map { link -> linkToDto(link) }
                .collect(Collectors.toList())
    }

    override fun dtoToLink(linkDto: LinkDto): Link {
        val link = Link()
        link.campaignId = linkDto.campaignId
        link.name = linkDto.name
        link.url = linkDto.url
        link.createdAt = LocalDateTime.now()
        link.uid = RandomStringUtils.randomAlphanumeric(8)
        return link
    }

    override fun linkToDto(link: Link): LinkDto {
        val linkDto = LinkDto()
        linkDto.id = link.id
        linkDto.campaignId = link.campaignId
        linkDto.createdAt = link.createdAt
        linkDto.name = link.name
        linkDto.uid = link.uid

        val allClickCount = clickRepository.countByLinkId(link.id!!)
        val validClickCount = clickRepository.countValidClicksByLinkId(link.id!!)
        linkDto.allClicks = allClickCount
        linkDto.allClicks = validClickCount
        return linkDto
    }

    private fun isValidUid(s: String): Boolean {
        val pattern = "^[a-zA-Z0-9]{8}$"
        return s.matches(pattern.toRegex())
    }

}