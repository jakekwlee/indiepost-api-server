package com.indiepost.utils

import com.indiepost.dto.LinkMetadataBookResponse
import com.indiepost.dto.LinkMetadataFlimResponse
import com.indiepost.dto.LinkMetadataResponse
import com.indiepost.enums.Types
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Entities
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

/**
 * Created by jake on 10/12/17.
 */
object DomUtil {
    fun htmlToText(html: String): String {
        val document = Jsoup.parseBodyFragment(html)
        return document.text()
    }

    fun getImagePrefixes(content: String): Set<String> {
        val pattern = Pattern.compile("\\d{4}/\\d{2}/(\\d{2}/)*\\w{6,8}")
        val imagePrefixList = LinkedHashSet<String>()

        val document = Jsoup.parseBodyFragment(content)
        val elements = document.select("img")

        for (element in elements) {
            val src = element.attr("src")
            val matcher = pattern.matcher(src)
            if (matcher.find()) {
                imagePrefixList.add(matcher.group())
            }
        }
        return imagePrefixList
    }

    fun extractMetadataFromUrl(urlString: String): LinkMetadataResponse? {
        val host: String?
        try {
            val urlComponents: UriComponents = UriComponentsBuilder.fromHttpUrl(urlString).build()
            host = urlComponents.host
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return null
        }
        val doc = Jsoup.connect(urlString).get()
        var title = doc.select("meta[property='og:title']")?.attr("content")
        if (title == null || title.isEmpty())
            title = doc.select("title").text()
        var url = doc.select("meta[property='og:url']").attr("content")
        if (url == null || url.isEmpty())
            url = urlString
        val image = doc.select("meta[property='og:image']")?.attr("content")
        val description = doc.select("meta[property='og:description']").attr("content")
        return LinkMetadataResponse(
                id = Random().nextLong(),
                title = title,
                description = description,
                imageUrl = image,
                source = host,
                url = url,
                type = Types.LinkBoxType.Default.toString()
        )
    }

    fun extractBookMetadataFromUrl(url: String): LinkMetadataBookResponse? {
        val host: String?
        val code: Long?
        try {
            val urlComponents: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()
            host = urlComponents.host
            if (host != "book.naver.com")
                return null
            code = urlComponents.queryParams["bid"]?.first()?.toLong()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return null
        }
        val doc = Jsoup.connect(url).get()
        val title = doc.select("meta[property='og:title']").attr("content")
        val image = doc.select("meta[property='og:image']").attr("content")
        val description = doc.select("meta[property='og:description']").attr("content")
        val bookInfo = doc.select("#container > div.spot > div.book_info > div.book_info_inner > div:nth-child(2)").text().split("|")
        val authors = bookInfo[0].replace("저자 ", "").split(", ")
        val publisher = bookInfo[1].trim()
        val pub = bookInfo[2]
//        var published: LocalDate? = null
//        if (pub.length == 8) {
//            published = LocalDate.parse(pub, DateTimeFormatter.ofPattern("yyyyMMdd"))
//        }
        return LinkMetadataBookResponse(
                id = code ?: Random().nextLong(),
                contentId = code,
                title = title,
                imageUrl = image,
                authors = authors,
                publisher = publisher,
                description = description,
                source = host,
                url = url,
                published = pub,
                type = Types.LinkBoxType.Book.toString()
        )
    }

    fun extractFlimMetadataFromUrl(url: String): LinkMetadataFlimResponse? {
        val host: String?
        val code: Long?
        try {
            val urlComponents: UriComponents = UriComponentsBuilder.fromHttpUrl(url).build()
            host = urlComponents.host
            if (host != "movie.naver.com")
                return null
            code = urlComponents.queryParams["code"]?.first()?.toLong()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return null
        }
        val doc = Jsoup.connect(url).get()
        val title = doc.select("meta[property='og:title']").attr("content")
        val image = doc.select("meta[property='og:image']").attr("content")
        val director = doc.select("div.mv_info > dl > dd:nth-child(4) > p").text()
        val actor = doc.select("div.mv_info > dl > dd:nth-child(6) > p").text()?.replace(Regex("\\(([^)]+)\\)"), "")
        val pub: String? = doc.select("div.mv_info > dl > dd:nth-child(2) > p > span:nth-child(4)")?.text()?.replace(Regex("[\\D.]"), "")
        var published: LocalDate? = null
        if (pub != null && pub.length == 8) {
            published = LocalDate.parse(pub, DateTimeFormatter.ofPattern("yyyyMMdd"))
        }
        return LinkMetadataFlimResponse(
                id = code ?: Random().nextLong(),
                contentId = code,
                title = title,
                imageUrl = image,
                directors = director?.split(", "),
                actors = actor?.split(", "),
                source = host,
                url = url,
                published = published?.year.toString(),
                type = Types.LinkBoxType.Flim.toString()
        )
    }

    fun findAndRemoveWriterInformationFromContent(content: String): String? {
        val html = Jsoup.parseBodyFragment(content)
        val settings = html.outputSettings()
        settings.syntax(Document.OutputSettings.Syntax.xml)
        settings.prettyPrint(false)
        settings.escapeMode(Entities.EscapeMode.extended)
        val postContent = html.select("p:contains(필자소개)")
        return if (postContent.isNotEmpty()) {
            postContent.prev().nextAll().remove()
            html.body().html().trim().trimIndent()
        } else {
            null
        }
    }
}
