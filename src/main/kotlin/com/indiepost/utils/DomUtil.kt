package com.indiepost.utils

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

    fun findAndRemoveWriterInformationFromContent(content: String): String? {
        val html = Jsoup.parseBodyFragment(content)
        val settings = html.outputSettings()
        settings.syntax(Document.OutputSettings.Syntax.xml)
        settings.prettyPrint(false)
        settings.escapeMode(Entities.EscapeMode.extended)
        val postContent = html.select("p:contains(필자소개)")
        if (postContent.isNotEmpty()) {
            postContent.prev().nextAll().remove()
            return html.body().html().trim().trimIndent()
        } else {
            return null
        }
    }
}
