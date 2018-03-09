package com.indiepost.utils;

import com.indiepost.dto.RelatedPostsMatchingResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jake on 10/12/17.
 */
public class DomUtil {
    public static Pattern relatedPostsPattern = Pattern.compile("<\\w+>[&;\\w\\s]*\\[{2}rel=\"[\\d,]+\"\\]{2}\\s*<\\/\\w+>");
    public static Pattern relatedIdsPattern = Pattern.compile("\\d+");

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

    public static RelatedPostsMatchingResult getRelatedPostIdsFromPostContent(String content) {
        Matcher matcher = relatedPostsPattern.matcher(content);
        if (matcher.find()) {
            String str = matcher.group();
            Matcher m = relatedIdsPattern.matcher(str);
            List<Long> ids = new ArrayList<>();
            while (m.find()) {
                Long id = Long.parseLong(m.group());
                ids.add(id);
            }
            String resultContent = matcher.replaceAll("");
            return new RelatedPostsMatchingResult(resultContent, ids);
        }
        return null;
    }
}
