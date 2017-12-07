package com.indiepost.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jake on 10/12/17.
 */
public class DomUtil {

    public static String htmlToText(String html) {
        Document document = Jsoup.parseBodyFragment(html);
        return document.text();
    }

    public static Set<String> getImagePrefixes(String content) {
        Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/(\\d{2}/)*\\w{6,8}");
        Set<String> imagePrefixList = new LinkedHashSet<>();

        Document document = Jsoup.parseBodyFragment(content);
        Elements elements = document.select("img");

        for (Element element : elements) {
            String src = element.attr("src");
            Matcher matcher = pattern.matcher(src);
            if (matcher.find()) {
                imagePrefixList.add(matcher.group());
            }
        }
        return imagePrefixList;
    }
}
