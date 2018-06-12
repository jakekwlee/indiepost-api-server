package com.indiepost.repository.utils;

import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainDoubleStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.enums.Types;

import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.utils.DateUtil.localDateTimeToDate;

public interface ResultMapper {

    static List<TimeDomainStat> toTimeDomainStatList(Query query,
                                                     Types.TimeDomainDuration duration,
                                                     LocalDateTime since,
                                                     LocalDateTime until) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        List<Object[]> result = query.getResultList();
        if (duration.equals(Types.TimeDomainDuration.HOURLY)) {
            return result.stream()
                    .map(obj -> {
                        Timestamp statDateTime = (Timestamp) obj[0];
                        BigInteger statValue = (BigInteger) obj[1];
                        return new TimeDomainStat(statDateTime.toLocalDateTime(), statValue.longValue());
                    }).collect(Collectors.toList());
        } else {
            return result.stream()
                    .map(obj -> {
                        Date statDateTime = (Date) obj[0];
                        BigInteger statValue = (BigInteger) obj[1];
                        return new TimeDomainStat(statDateTime.toLocalDate().atStartOfDay(), statValue.longValue());
                    }).collect(Collectors.toList());

        }
    }

    static List<TimeDomainDoubleStat> toTimeDomainDoubleStatList(Query query,
                                                                 Types.TimeDomainDuration duration,
                                                                 LocalDateTime since,
                                                                 LocalDateTime until) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        List<Object[]> result = query.getResultList();
        if (duration.equals(Types.TimeDomainDuration.HOURLY)) {
            return result.stream()
                    .map(obj -> {
                        Timestamp statDateTime = (Timestamp) obj[0];
                        BigInteger statValue1 = (BigInteger) obj[1];
                        BigInteger statValue2 = (BigInteger) obj[2];
                        return new TimeDomainDoubleStat(
                                statDateTime.toLocalDateTime(),
                                statValue1.longValue(),
                                statValue2.longValue()
                        );
                    }).collect(Collectors.toList());
        } else {
            return result.stream()
                    .map(obj -> {
                        Date statDateTime = (Date) obj[0];
                        BigInteger statValue1 = (BigInteger) obj[1];
                        BigInteger statValue2 = (BigInteger) obj[2];
                        return new TimeDomainDoubleStat(
                                statDateTime.toLocalDate().atStartOfDay(),
                                statValue1.longValue(),
                                statValue2.longValue());
                    }).collect(Collectors.toList());

        }
    }

    static List<ShareStat> toShareStateList(Query query, LocalDateTime since, LocalDateTime until, Integer limit) {
        return toShareStateList(query, since, until, limit, null);
    }

    static List<ShareStat> toShareStateList(Query query, LocalDateTime since, LocalDateTime until, Integer limit, String client) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setParameter("limit", limit);
        if (client != null) {
            query.setParameter("client", client);
        }
        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(objects -> new ShareStat((String) objects[0], ((BigInteger) objects[1]).longValue()))
                .collect(Collectors.toList());
    }

    static List<PostStatDto> toPostStatDtoList(Query query) {
        return toPostStatDtoList(query, null, null, null);
    }

    static List<PostStatDto> toPostStatDtoList(Query query, LocalDateTime since, LocalDateTime until, Integer limit) {
        if (since != null) {
            query.setParameter("since", localDateTimeToDate(since));
        }
        if (until != null) {
            query.setParameter("until", localDateTimeToDate(until));
        }
        if (limit != null) {
            query.setParameter("limit", limit);
        }
        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(obj -> {
                    Long id = ((BigInteger) obj[0]).longValue();
                    String title = (String) obj[1];
                    LocalDateTime publishedAt = ((Timestamp) obj[2]).toLocalDateTime();
                    String author = (String) obj[3];
                    String category = (String) obj[4];
                    Long pageviews = ((BigInteger) obj[5]).longValue();
                    Long uniquePageviews = ((BigInteger) obj[6]).longValue();

                    PostStatDto dto = new PostStatDto();
                    if (obj.length == 9) {
                        Long legacyPageviews = ((BigInteger) obj[7]).longValue();
                        Long legacyUniquePageviews = ((BigInteger) obj[8]).longValue();
                        dto.setLegacyPageviews(legacyPageviews);
                        dto.setLegacyUniquePageviews(legacyUniquePageviews);
                    }
                    dto.setId(id);
                    dto.setTitle(title);
                    dto.setPublishedAt(publishedAt);
                    dto.setAuthor(author);
                    dto.setCategory(category);
                    dto.setPageviews(pageviews);
                    dto.setUniquePageviews(uniquePageviews);
                    return dto;
                }).collect(Collectors.toList());
    }
}
