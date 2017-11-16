package com.indiepost.repository.utils;

import com.indiepost.dto.analytics.PostStatDto;
import org.hibernate.transform.ResultTransformer;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jake on 10/24/17.
 */
public class PostStatsResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        PostStatDto postStatDto = new PostStatDto();
        for (int i = 0; i < tuple.length; ++i) {
            switch (aliases[i]) {
                case "id":
                    postStatDto.setId(((BigInteger) tuple[i]).longValue());
                    break;
                case "title":
                    postStatDto.setTitle((String) tuple[i]);
                    break;
                case "author":
                    postStatDto.setAuthor((String) tuple[i]);
                    break;
                case "category":
                    postStatDto.setCategory((String) tuple[i]);
                    break;
                case "publishedAt":
                    postStatDto.setPublishedAt(((Timestamp) tuple[i]).toLocalDateTime());
                    break;
                case "pageviews":
                    postStatDto.setPageviews(((BigInteger) tuple[i]).longValue());
                    break;
                case "uniquePageviews":
                    postStatDto.setUniquePageviews(((BigInteger) tuple[i]).longValue());
                    break;
            }
        }
        return postStatDto;
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
