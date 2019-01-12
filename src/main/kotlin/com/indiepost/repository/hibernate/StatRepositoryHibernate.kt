package com.indiepost.repository.hibernate

import com.indiepost.dto.analytics.PostStatDto
import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainDoubleStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.enums.Types
import com.indiepost.enums.Types.ClientType
import com.indiepost.enums.Types.TimeDomainDuration
import com.indiepost.model.analytics.Stat
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.StatRepository
import com.indiepost.repository.utils.ResultMapper.toPostStatDtoList
import com.indiepost.repository.utils.ResultMapper.toShareStateList
import com.indiepost.repository.utils.ResultMapper.toTimeDomainDoubleStatList
import com.indiepost.repository.utils.ResultMapper.toTimeDomainStatList
import com.indiepost.utils.DateUtil.localDateTimeToDate
import com.indiepost.utils.DateUtil.normalizeTimeDomainStats
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

/**
 * Created by jake on 8/9/17.
 */
@Repository
class StatRepositoryHibernate : StatRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getAllPostStats(): List<PostStatDto> {
        val query = getNamedQuery("@GET_ALL_POST_STATS")
        return toPostStatDtoList(query)
    }

    override fun getCachedPostStats(): List<PostStatDto> {
        val query = getNamedQuery("@GET_ALL_POST_STATS_FROM_CACHE")
        return toPostStatDtoList(query)
    }

    private val session: Session
        get() = entityManager.unwrap(Session::class.java)

    override fun save(stat: Stat): Long? {
        if (stat.visitorId != null) {
            val visitorReference = entityManager.getReference(Visitor::class.java, stat.visitorId)
            stat.visitor = visitorReference
        }
        entityManager.persist(stat)
        return stat.id
    }

    override fun delete(stat: Stat) {
        entityManager.remove(stat)
    }

    override fun findOne(id: Long): Stat {
        return entityManager.find(Stat::class.java, id)
    }

    override fun getTotalPageviews(since: LocalDateTime, until: LocalDateTime): Long {
        val criteria = createCriteria()
        criteria.createAlias("visitor", "v")
        criteria.add(Restrictions.ne("s.class", "Click"))
        criteria.add(Restrictions.between("s.timestamp", since, until))
        criteria.add(Restrictions.ne("v.appName", ClientType.INDIEPOST_AD_ENGINE.toString()))
        criteria.setProjection(Projections.rowCount())
        return criteria.uniqueResult() as Long
    }

    override fun getTotalPageviews(since: LocalDateTime, until: LocalDateTime, client: String): Long {
        val criteria = createCriteria()
        criteria.createAlias("visitor", "v")
        criteria.add(Restrictions.ne("s.class", "Click"))
        criteria.add(Restrictions.between("s.timestamp", since, until))
        criteria.add(Restrictions.eq("v.appName", client))
        criteria.setProjection(Projections.rowCount())
        return criteria.uniqueResult() as Long
    }

    override fun getTotalPostviews(since: LocalDateTime, until: LocalDateTime): Long {
        val criteria = createCriteria()
        criteria.createAlias("visitor", "v")
        criteria.add(Restrictions.isNotNull("s.postId"))
        criteria.add(Restrictions.ne("s.class", "Click"))
        criteria.add(Restrictions.between("s.timestamp", since, until))
        criteria.add(Restrictions.ne("v.appName", ClientType.INDIEPOST_AD_ENGINE.toString()))
        criteria.setProjection(Projections.rowCount())
        return criteria.uniqueResult() as Long
    }

    override fun getTotalPostviews(since: LocalDateTime, until: LocalDateTime, client: String): Long {
        val criteria = createCriteria()
        criteria.createAlias("visitor", "v")
        criteria.add(Restrictions.isNotNull("s.postId"))
        criteria.add(Restrictions.ne("s.class", "Click"))
        criteria.add(Restrictions.between("s.timestamp", since, until))
        criteria.add(Restrictions.eq("v.appName", client))
        criteria.setProjection(Projections.rowCount())
        return criteria.uniqueResult() as Long
    }

    override fun getTotalUniquePageviews(since: LocalDateTime, until: LocalDateTime): Long {
        val query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS")
        query.setParameter("since", localDateTimeToDate(since))
        query.setParameter("until", localDateTimeToDate(until))
        return (query.singleResult as BigInteger).toLong()
    }

    override fun getTotalUniquePageviews(since: LocalDateTime, until: LocalDateTime, client: String): Long {
        val query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_BY_CLIENT")
        query.setParameter("client", client)
        query.setParameter("since", localDateTimeToDate(since))
        query.setParameter("until", localDateTimeToDate(until))
        return (query.singleResult as BigInteger).toLong()
    }

    override fun getTotalUniquePostviews(since: LocalDateTime, until: LocalDateTime): Long {
        val query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS")
        query.setParameter("since", localDateTimeToDate(since))
        query.setParameter("until", localDateTimeToDate(until))
        return (query.singleResult as BigInteger).toLong()
    }

    override fun getTotalUniquePostviews(since: LocalDateTime, until: LocalDateTime, client: String): Long {
        val query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS_BY_CLIENT")
        query.setParameter("client", client)
        query.setParameter("since", localDateTimeToDate(since))
        query.setParameter("until", localDateTimeToDate(until))
        return (query.singleResult as BigInteger).toLong()
    }

    override fun getPageviewTrend(since: LocalDateTime, until: LocalDateTime, duration: TimeDomainDuration): List<TimeDomainStat> {
        when (duration) {
            Types.TimeDomainDuration.HOURLY -> return getPageviewTrendHourly(since, until)
            Types.TimeDomainDuration.DAILY -> return getPageviewTrendDaily(since, until)
            Types.TimeDomainDuration.MONTHLY -> return getPageviewTrendMonthly(since, until)
            Types.TimeDomainDuration.YEARLY -> return getPageviewTrendYearly(since, until)
            else -> return getPageviewTrendHourly(since, until)
        }
    }

    private fun getPageviewTrendHourly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_PAGEVIEW_TREND_HOURLY")
        val trend = toTimeDomainStatList(query, TimeDomainDuration.HOURLY, since, until)
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate())
    }

    private fun getPageviewTrendDaily(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_PAGEVIEW_TREND_DAILY")
        return toTimeDomainStatList(query, TimeDomainDuration.DAILY, since, until)
    }

    private fun getPageviewTrendMonthly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_PAGEVIEW_TREND_MONTHLY")
        return toTimeDomainStatList(query, TimeDomainDuration.MONTHLY, since, until)
    }

    private fun getPageviewTrendYearly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_PAGEVIEW_TREND_YEARLY")
        return toTimeDomainStatList(query, TimeDomainDuration.YEARLY, since, until)
    }

    override fun getRecentAndOldPageviewTrend(since: LocalDateTime, until: LocalDateTime, duration: TimeDomainDuration): List<TimeDomainDoubleStat> {
        when (duration) {
            Types.TimeDomainDuration.HOURLY -> return getOldAndNewPageviewTrendHourly(since, until)
            Types.TimeDomainDuration.DAILY -> return getOldAndNewPageviewTrendDaily(since, until)
            Types.TimeDomainDuration.MONTHLY -> return getOldAndNewPageviewTrendMonthly(since, until)
            Types.TimeDomainDuration.YEARLY -> return getOldAndNewPageviewTrendYearly(since, until)
            else -> return getOldAndNewPageviewTrendHourly(since, until)
        }
    }

    private fun getOldAndNewPageviewTrendHourly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainDoubleStat> {
        val query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_HOURLY")
        return toTimeDomainDoubleStatList(query, TimeDomainDuration.HOURLY, since, until)
    }

    private fun getOldAndNewPageviewTrendDaily(since: LocalDateTime, until: LocalDateTime): List<TimeDomainDoubleStat> {
        val query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_DAILY")
        return toTimeDomainDoubleStatList(query, TimeDomainDuration.DAILY, since, until)
    }

    private fun getOldAndNewPageviewTrendMonthly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainDoubleStat> {
        val query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_MONTHLY")
        return toTimeDomainDoubleStatList(query, TimeDomainDuration.MONTHLY, since, until)
    }

    private fun getOldAndNewPageviewTrendYearly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainDoubleStat> {
        val query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_YEARLY")
        return toTimeDomainDoubleStatList(query, TimeDomainDuration.YEARLY, since, until)
    }

    override fun getPostStatsOrderByPageviews(since: LocalDateTime, until: LocalDateTime, limit: Int): List<PostStatDto> {
        val query = getNamedQuery("@GET_POST_STATS_ORDER_BY_PAGEVIEWS")
        return toPostStatDtoList(query, since, until, limit)
    }

    override fun getPageviewsByPrimaryTag(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_PAGEVIEWS_ORDER_BY_PRIMARY_TAG")
        return toShareStateList(query, since, until, limit)
    }

    override fun getPageviewsByAuthor(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_PAGEVIEWS_ORDER_BY_AUTHOR")
        return toShareStateList(query, since, until, limit)
    }

    override fun getTopPages(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_PAGES")
        return toShareStateList(query, since, until, limit)
    }

    override fun getTopPages(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_PAGES_BY_CLINT_TYPE")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_POSTS")
        return toShareStateList(query, since, until, limit)
    }

    override fun getTopPosts(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_POSTS_BY_CLINT_TYPE")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopLandingPages(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_LANDING_PAGE")
        return toShareStateList(query, since, until, limit)
    }

    override fun getTopLandingPages(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_LANDING_PAGE_BY_CLINT_TYPE")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopTags(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_TAGS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopTags(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_TAGS_BY_CLIENT")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopRecentPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_RECENT_POSTS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopOldPosts(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_OLD_POSTS")
        return toShareStateList(query, since, until, limit, null)
    }

    private fun getNamedQuery(queryName: String): Query {
        return entityManager.createNamedQuery(queryName)
    }

    private fun createCriteria(): Criteria {
        return session.createCriteria(Stat::class.java, "s")
    }
}
