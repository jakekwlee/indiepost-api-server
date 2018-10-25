package com.indiepost.repository

import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.enums.Types
import com.indiepost.model.analytics.Visitor

import java.time.LocalDateTime

/**
 * Created by jake on 17. 4. 9.
 */
interface VisitorRepository {

    fun save(visitor: Visitor): Long?

    fun delete(visitor: Visitor)

    fun findOne(id: Long): Visitor?

    fun getTotalVisitors(since: LocalDateTime, until: LocalDateTime): Long

    fun getTotalVisitors(since: LocalDateTime, until: LocalDateTime, client: String): Long

    fun getVisitorTrend(since: LocalDateTime, until: LocalDateTime, duration: Types.TimeDomainDuration): List<TimeDomainStat>

    fun getTopReferrers(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopReferrers(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopWebBrowsers(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopWebBrowsers(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopOs(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopOs(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>

    fun getTopChannel(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat>

    fun getTopChannel(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat>
}