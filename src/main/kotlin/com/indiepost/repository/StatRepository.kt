package com.indiepost.repository

import com.indiepost.dto.analytics.PostStatDto
import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainDoubleStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.enums.Types.TimeDomainDuration
import com.indiepost.model.analytics.Stat

import java.time.LocalDateTime

/**
 * Created by jake on 8/9/17.
 */
interface StatRepository {

    fun getAllPostStats(): List<PostStatDto>

    fun getCachedPostStats(): List<PostStatDto>

    fun save(stat: Stat): Long?

    fun delete(stat: Stat)

    fun findOne(id: Long): Stat?

    fun getTotalPageviews(since: LocalDateTime, until: LocalDateTime): Long

    fun getTotalPageviews(since: LocalDateTime, until: LocalDateTime, client: String): Long

    fun getTotalPostviews(since: LocalDateTime, until: LocalDateTime): Long

    fun getTotalPostviews(since: LocalDateTime, until: LocalDateTime, client: String): Long

    fun getTotalUniquePageviews(since: LocalDateTime, until: LocalDateTime): Long

    fun getTotalUniquePageviews(since: LocalDateTime, until: LocalDateTime, client: String): Long

    fun getTotalUniquePostviews(since: LocalDateTime, until: LocalDateTime): Long

    fun getTotalUniquePostviews(since: LocalDateTime, until: LocalDateTime, client: String): Long

    fun getPageviewTrend(since: LocalDateTime, until: LocalDateTime, duration: TimeDomainDuration): List<TimeDomainStat>

    fun getRecentAndOldPageviewTrend(since: LocalDateTime, until: LocalDateTime, duration: TimeDomainDuration): List<TimeDomainDoubleStat>

    fun getPostStatsOrderByPageviews(since: LocalDateTime, until: LocalDateTime, limit: Int): List<PostStatDto>

    fun getPageviewsByPrimaryTag(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getPageviewsByAuthor(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopPages(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopPages(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopPosts(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopLandingPages(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopLandingPages(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopTags(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopTags(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopRecentPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopOldPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

}
