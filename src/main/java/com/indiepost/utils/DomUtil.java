package com.indiepost.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jake on 10/12/17.
 */
public class DomUtil {
    public static List<String> getImagePrefixes(String content) {
        Pattern pattern = Pattern.compile("\\d{4}/\\d{2}/\\w{6,8}");
        List<String> imagePrefixList = new ArrayList<>();

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
