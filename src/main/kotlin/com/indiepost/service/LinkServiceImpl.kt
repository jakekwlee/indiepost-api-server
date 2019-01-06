package com.indiepost.service

import com.indiepost.dto.LinkMetadataResponse
import com.indiepost.dto.analytics.LinkDto
import com.indiepost.enums.Types
import com.indiepost.model.analytics.Link
import com.indiepost.repository.ClickRepository
import com.indiepost.repository.LinkRepository
import com.indiepost.utils.DomUtil.extractMetadataFromUrl
import com.mashape.unirest.http.Unirest
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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

    override fun getFromUrl(url: String): LinkMetadataResponse? {
        return extractMetadataFromUrl(url)
    }

    override fun searchBooks(text: String, limit: Int): List<LinkMetadataResponse> {
        val response = Unirest.get("https://openapi.naver.com/v1/search/book_adv").header("Content-Type", "plain/text")
                .header("X-Naver-Client-Id", "9Q61_ESCrPSL3Tni3laW")
                .header("X-Naver-Client-Secret", "YjMHi7Gcoe")
                .queryString("d_titl", text)
                .queryString("display", limit)
                .asJson()

        val tagRegex = Regex("<[^>]*>")
        val body = response.body.`object`
        if (body.get("total") == 0) {
            return emptyList()
        } else {
            val arr = body.getJSONArray("items")
            return arr.toList().stream().map { item ->
                val data = item as HashMap<String, String>
                val title = data["title"]
                        ?.replace(tagRegex, "")
                val url = data["link"] ?: "https://indiepost.co.kr"
                val urlComponents: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()
                val code = urlComponents.queryParams["bid"]?.first()?.toLong()
                val pubished = LocalDate.parse(data["pubdate"], DateTimeFormatter.ofPattern("yyyyMMdd"))
                val authors = data["author"]
                        ?.split("|")
                        ?.filter { it != "" }
                        ?.map { it.replace(tagRegex, "") }
                val publisher = data["publisher"]
                val imageUrl = data["image"]
                val description = data["description"]
                LinkMetadataResponse(
                        id = code ?: Random().nextLong(),
                        productId = code,
                        title = title,
                        url = url,
                        authors = authors,
                        publisher = publisher,
                        description = description,
                        source = "book.naver.com",
                        type = Types.LinkBoxType.Book.toString(),
                        imageUrl = imageUrl,
                        published = pubished.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                )
            }
                    .collect(Collectors.toList())
        }
    }

    override fun searchMovies(text: String, limit: Int): List<LinkMetadataResponse> {
        val response = Unirest.get("https://openapi.naver.com/v1/search/movie").header("Content-Type", "plain/text")
                .header("X-Naver-Client-Id", "9Q61_ESCrPSL3Tni3laW")
                .header("X-Naver-Client-Secret", "YjMHi7Gcoe")
                .queryString("query", text)
                .queryString("display", limit)
                .asJson()

        val tagRegex = Regex("<[^>]*>")
        val body = response.body.`object`
        if (body.get("total") == 0) {
            return emptyList()
        } else {
            val arr = body.getJSONArray("items")
            return arr.toList().stream().map { item ->
                val data = item as HashMap<String, String>
                val title = data["title"]
                        ?.replace(tagRegex, "")
                val url = data["link"] ?: "https://indiepost.co.kr"
                val urlComponents: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()
                val code = urlComponents.queryParams["code"]?.first()?.toLong()
                val published = data["pubDate"]
                val directors = data["director"]
                        ?.split("|")
                        ?.filter { it != "" }
                        ?.map { it.replace(tagRegex, "") }
                val actors = data["actor"]
                        ?.split("|")
                        ?.filter { it != "" }
                        ?.map { it.replace(tagRegex, "") }
                val imageUrl = data["image"]
                LinkMetadataResponse(
                        id = code ?: Random().nextLong(),
                        productId = code,
                        title = title,
                        url = url,
                        directors = directors,
                        actors = actors,
                        source = "movie.naver.com",
                        type = Types.LinkBoxType.Film.toString(),
                        imageUrl = imageUrl,
                        published = published
                )
            }
                    .collect(Collectors.toList())

        }
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
