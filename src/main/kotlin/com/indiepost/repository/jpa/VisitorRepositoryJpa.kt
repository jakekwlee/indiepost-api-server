package com.indiepost.repository.jpa

import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.enums.Types
import com.indiepost.enums.Types.TimeDomainDuration
import com.indiepost.model.User
import com.indiepost.model.analytics.QVisitor
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.VisitorRepository
import com.indiepost.repository.utils.ResultMapper.toShareStateList
import com.indiepost.repository.utils.ResultMapper.toTimeDomainStatList
import com.indiepost.utils.DateUtil.normalizeTimeDomainStats
import com.querydsl.jpa.impl.JPAQueryFactory
import org.hibernate.Session
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

/**
 * Created by jake on 8/9/17.
 */
@Repository
class VisitorRepositoryJpa : VisitorRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val qVisitor = QVisitor.visitor

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(visitor: Visitor): Long? {
        if (visitor.userId != null) {
            val userReference = entityManager.getReference(User::class.java, visitor.userId)
            visitor.user = userReference
        }
        entityManager.persist(visitor)
        return visitor.id
    }

    override fun delete(visitor: Visitor) {
        entityManager.remove(visitor)
    }

    override fun findOne(id: Long): Visitor {
        return entityManager.find(Visitor::class.java, id)
    }

    override fun getTotalVisitors(since: LocalDateTime, until: LocalDateTime): Long {
        return queryFactory.selectFrom(qVisitor)
                .where(qVisitor.timestamp.between(since, until)
                        .and(qVisitor.isAdVisitor.isFalse))
                .fetchCount()
    }

    override fun getTotalVisitors(since: LocalDateTime, until: LocalDateTime, client: String): Long {
        return queryFactory.selectFrom(qVisitor)
                .where(qVisitor.timestamp.between(since, until)
                        .and(qVisitor.appName.eq(client)))
                .fetchCount()
    }

    override fun getVisitorTrend(since: LocalDateTime, until: LocalDateTime, duration: TimeDomainDuration): List<TimeDomainStat> {
        when (duration) {
            Types.TimeDomainDuration.HOURLY -> return getVisitorTrendHourly(since, until)
            Types.TimeDomainDuration.DAILY -> return getVisitorTrendDaily(since, until)
            Types.TimeDomainDuration.MONTHLY -> return getVisitorTrendMonthly(since, until)
            Types.TimeDomainDuration.YEARLY -> return getVisitorTrendYearly(since, until)
            else -> return getVisitorTrendHourly(since, until)
        }
    }

    private fun getVisitorTrendHourly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_VISITORS_TREND_HOURLY")
        val trend = toTimeDomainStatList(query, TimeDomainDuration.HOURLY, since, until)
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate())
    }

    private fun getVisitorTrendDaily(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_VISITORS_TREND_DAILY")
        return toTimeDomainStatList(query, TimeDomainDuration.DAILY, since, until)
    }

    private fun getVisitorTrendMonthly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_VISITORS_TREND_MONTHLY")
        return toTimeDomainStatList(query, TimeDomainDuration.MONTHLY, since, until)
    }

    private fun getVisitorTrendYearly(since: LocalDateTime, until: LocalDateTime): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_VISITORS_TREND_YEARLY")
        return toTimeDomainStatList(query, TimeDomainDuration.YEARLY, since, until)
    }

    override fun getTopReferrers(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_REFERRERS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopReferrers(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_REFERRERS_BY_CLIENT")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopWebBrowsers(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_WEB_BROWSERS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopWebBrowsers(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_WEB_BROWSERS_BY_CLIENT")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopOs(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_OS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopOs(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_OS_BY_CLIENT")
        return toShareStateList(query, since, until, limit, client)
    }

    override fun getTopChannel(since: LocalDateTime, until: LocalDateTime, limit: Int): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_CHANNELS")
        return toShareStateList(query, since, until, limit, null)
    }

    override fun getTopChannel(since: LocalDateTime, until: LocalDateTime, limit: Int, client: String): List<ShareStat> {
        val query = getNamedQuery("@GET_TOP_CHANNELS_BY_CLIENT")
        return toShareStateList(query, since, until, limit, client)
    }

    private fun getNamedQuery(queryName: String): Query {
        return entityManager.unwrap(Session::class.java).getNamedQuery(queryName)
    }
}
