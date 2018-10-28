package com.indiepost.utils

import org.jsoup.Jsoup
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
}
