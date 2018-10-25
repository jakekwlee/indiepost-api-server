package com.indiepost.repository.utils

import com.indiepost.dto.analytics.PostStatDto
import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainDoubleStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.enums.Types
import com.indiepost.utils.DateUtil.localDateTimeToDate
import java.math.BigInteger
import java.sql.Date
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.Query

fun toTimeDomainStatList(query: Query,
                         duration: Types.TimeDomainDuration,
                         since: LocalDateTime,
                         until: LocalDateTime): List<TimeDomainStat> {
    query.setParameter("since", localDateTimeToDate(since))
    query.setParameter("until", localDateTimeToDate(until))
    val result = query.resultList
    if (duration == Types.TimeDomainDuration.HOURLY) {
        return result.stream()
                .map { obj ->
                    val o = obj as Array<*>
                    val statDateTime = o[0] as Timestamp
                    val statValue = o[1] as BigInteger
                    TimeDomainStat(statDateTime.toLocalDateTime(), statValue.toLong())
                }.collect(Collectors.toList())
    } else {
        return result.stream()
                .map { obj ->
                    val o = obj as Array<*>
                    val statDateTime = o[0] as Date
                    val statValue = o[1] as BigInteger
                    TimeDomainStat(statDateTime.toLocalDate().atStartOfDay(), statValue.toLong())
                }.collect(Collectors.toList<TimeDomainStat>())

    }
}

fun toTimeDomainDoubleStatList(query: Query,
                               duration: Types.TimeDomainDuration,
                               since: LocalDateTime,
                               until: LocalDateTime): List<TimeDomainDoubleStat> {
    query.setParameter("since", localDateTimeToDate(since))
    query.setParameter("until", localDateTimeToDate(until))
    val result = query.resultList
    if (duration == Types.TimeDomainDuration.HOURLY) {
        return result.stream()
                .map { obj ->
                    val o = obj as Array<*>
                    val statDateTime = o[0] as Timestamp
                    val statValue1 = o[1] as BigInteger
                    val statValue2 = o[2] as BigInteger
                    TimeDomainDoubleStat(
                            statDateTime.toLocalDateTime(),
                            statValue1.toLong(),
                            statValue2.toLong()
                    )
                }.collect(Collectors.toList())
    } else {
        return result.stream()
                .map { obj ->
                    val o = obj as Array<*>
                    val statDateTime = o[0] as Date
                    val statValue1 = o[1] as BigInteger
                    val statValue2 = o[2] as BigInteger
                    TimeDomainDoubleStat(
                            statDateTime.toLocalDate().atStartOfDay(),
                            statValue1.toLong(),
                            statValue2.toLong())
                }.collect(Collectors.toList<TimeDomainDoubleStat>())

    }
}

@JvmOverloads
fun toShareStateList(query: Query, since: LocalDateTime, until: LocalDateTime, limit: Int?, client: String? = null): List<ShareStat> {
    query.setParameter("since", localDateTimeToDate(since))
    query.setParameter("until", localDateTimeToDate(until))
    query.setParameter("limit", limit)
    if (client != null) {
        query.setParameter("client", client)
    }
    val result = query.resultList
    return result.stream()
            .map { objects ->
                val o = objects as Array<*>
                ShareStat(o[0] as String, (o[1] as BigInteger).toLong())
            }
            .collect(Collectors.toList())
}

@JvmOverloads
fun toPostStatDtoList(query: Query, since: LocalDateTime? = null, until: LocalDateTime? = null, limit: Int? = null): List<PostStatDto> {
    if (since != null) {
        query.setParameter("since", localDateTimeToDate(since))
    }
    if (until != null) {
        query.setParameter("until", localDateTimeToDate(until))
    }
    if (limit != null) {
        query.setParameter("limit", limit)
    }
    val result = query.resultList
    return result.stream()
            .map { obj ->
                val o = obj as Array<*>
                val id = (o[0] as BigInteger).toLong()
                val title = o[1] as String
                val publishedAt = (o[2] as Timestamp).toLocalDateTime()
                val author = o[3] as String
                val category = o[4] as String
                val pageviews = (o[5] as BigInteger).toLong()
                val uniquePageviews = (o[6] as BigInteger).toLong()

                val dto = PostStatDto()
                if (obj.size == 9) {
                    val legacyPageviews = (o[7] as BigInteger).toLong()
                    val legacyUniquePageviews = (o[8] as BigInteger).toLong()
                    dto.legacyPageviews = legacyPageviews
                    dto.legacyUniquePageviews = legacyUniquePageviews
                }
                dto.id = id
                dto.title = title
                dto.publishedAt = publishedAt
                dto.author = author
                dto.category = category
                dto.pageviews = pageviews
                dto.uniquePageviews = uniquePageviews
                dto
            }.collect(Collectors.toList())
}
