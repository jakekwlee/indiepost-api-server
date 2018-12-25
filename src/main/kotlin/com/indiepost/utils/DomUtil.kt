package com.indiepost.utils

import com.indiepost.dto.LinkBoxResponse
import com.indiepost.enums.Types
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Entities
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

    fun extractInformationFromURL(url: String): LinkBoxResponse? {
        val urlSlices = url.split("/")
        val source = if (urlSlices.size > 2) urlSlices[2] else null
        if (source != "movie.naver.com") {
            return null
        }
        val doc = Jsoup.connect(url).get()
        val title = doc.select("meta[property='og:title']").attr("content")
        val image = doc.select("meta[property='og:image']").attr("content")
        val director = doc.select("div.mv_info > dl > dd:nth-child(4) > p").text()
        val credits = doc.select("div.mv_info > dl > dd:nth-child(6) > p").text()
        return LinkBoxResponse(
                title = title,
                imageUrl = image,
                data = Arrays.asList(director, credits),
                source = source,
                url = url,
                type = Types.LinkBoxType.Movie.toString()
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
