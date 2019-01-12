package com.indiepost.service

import com.indiepost.dto.analytics.*
import com.indiepost.enums.Types.ClientType
import com.indiepost.repository.MetadataRepository
import com.indiepost.repository.StatRepository
import com.indiepost.repository.UserRepository
import com.indiepost.repository.VisitorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional(readOnly = true)
class AnalyticsServiceImpl @Inject constructor(private val metadataRepository: MetadataRepository,
                                               private val statRepository: StatRepository,
                                               private val visitorRepository: VisitorRepository,
                                               private val userRepository: UserRepository) : AnalyticsService {

    override fun getOverviewStats(periodDto: PeriodDto): OverviewStats {
        val startDate = periodDto.startDate
        val endDate = periodDto.endDate
        val since = startDate!!.atStartOfDay()
        val until = endDate!!.atTime(23, 59, 59)

        val pageviewStatResult: List<TimeDomainStat>
        val visitorTrendResult: List<TimeDomainStat>

        val duration = periodDto.duration
        pageviewStatResult = statRepository.getPageviewTrend(since, until, duration!!)
        visitorTrendResult = visitorRepository.getVisitorTrend(since, until, duration)

        val pageviewTrend = Trend()
        val visitorTrend = Trend()
        pageviewTrend.duration = duration
        pageviewTrend.result = pageviewStatResult
        visitorTrend.duration = duration
        visitorTrend.result = visitorTrendResult

        val stats = OverviewStats()
        stats.pageviewTrend = pageviewTrend
        stats.visitorTrend = visitorTrend
        stats.period = periodDto

        stats.totalPageview = statRepository.getTotalPageviews(since, until)
        stats.totalUniquePageview = statRepository.getTotalUniquePageviews(since, until)
        stats.totalPostview = statRepository.getTotalPostviews(since, until)
        stats.totalUniquePostview = statRepository.getTotalUniquePostviews(since, until)

        stats.totalVisitor = visitorRepository.getTotalVisitors(since, until)
        stats.totalAppVisitor = visitorRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString())

        stats.totalUsers = userRepository.totalUsers
        stats.newSignups = userRepository.getTotalUsers(LocalDateTime.now().minusDays(7), LocalDateTime.now())

        return stats
    }

    override fun getTopStats(periodDto: PeriodDto): TopStats {
        val startDate = periodDto.startDate
        val endDate = periodDto.endDate
        val since = startDate!!.atStartOfDay()
        val until = endDate!!.atTime(23, 59, 59)

        val stats = TopStats()
        stats.period = periodDto
        stats.topPagesWebapp = statRepository.getTopPages(since, until, 10)
        stats.topPosts = statRepository.getTopPosts(since, until, 10)
        stats.pageviewByAuthor = statRepository.getPageviewsByAuthor(since, until, 10)
        stats.pageviewByPrimaryTag = statRepository.getPageviewsByPrimaryTag(since, until, 10)
        stats.topBrowser = visitorRepository.getTopWebBrowsers(since, until, 10)
        stats.topChannel = visitorRepository.getTopChannel(since, until, 10)
        stats.topReferrer = visitorRepository.getTopReferrers(since, until, 10)
        stats.topOs = visitorRepository.getTopOs(since, until, 10)
        return stats
    }


    override fun getRecentAndOldPostStats(periodDto: PeriodDto): RecentAndOldPostStats {
        val startDate = periodDto.startDate
        val endDate = periodDto.endDate
        val since = startDate!!.atStartOfDay()
        val until = endDate!!.atTime(23, 59, 59)
        val duration = periodDto.duration

        val resultStats = statRepository.getRecentAndOldPageviewTrend(since, until, duration!!)
        val trend = DoubleTrend()
        trend.duration = duration
        trend.result = resultStats
        trend.statName = "Time domain old and new post pageviews trend"

        val stats = RecentAndOldPostStats()
        stats.trend = trend
        stats.period = periodDto
        stats.topRecentPosts = statRepository.getTopRecentPosts(since, until, 10)
        stats.topOldPosts = statRepository.getTopOldPosts(since, until, 10)
        return stats
    }

    override fun getCachedPostStats(): PostStatsDto? {
        val statData = statRepository.getCachedPostStats()
        val optional = metadataRepository.findById(1L)
        if (optional.isPresent) {
            val (_, lastUpdated) = optional.get()
            return PostStatsDto(lastUpdated!!, statData)
        } else {
            return null
        }

    }

    override fun getPostStats(periodDto: PeriodDto): PostStatsDto {
        val startDate = periodDto.startDate
        val endDate = periodDto.endDate
        val since = startDate!!.atStartOfDay()
        val until = endDate!!.atTime(23, 59, 59)
        val postStatDtoList = statRepository.getPostStatsOrderByPageviews(since, until, 3000)
        return PostStatsDto(LocalDateTime.now(), postStatDtoList, periodDto)
    }
}
