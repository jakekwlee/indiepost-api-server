package com.indiepost.repository.jpa

import com.indiepost.dto.analytics.*
import com.indiepost.enums.Types.LinkType
import com.indiepost.model.Banner
import com.indiepost.model.QBanner
import com.indiepost.model.analytics.*
import com.indiepost.repository.CampaignRepository
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.jpa.impl.JPAQueryFactory
import org.hibernate.Session
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

@Repository
class CampaignRepositoryJpa : CampaignRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val c = QCampaign.campaign

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(campaign: Campaign) {
        entityManager.persist(campaign)
    }

    override fun findOne(id: Long): Campaign? {
        return entityManager.find(Campaign::class.java, id)
    }

    override fun deleteById(id: Long) {
        val campaignReference = entityManager.getReference(Campaign::class.java, id)
        entityManager.remove(campaignReference)
    }

    override fun count(): Long {
        return queryFactory
                .selectFrom(c)
                .fetchCount()
    }

    override fun find(pageable: Pageable): Page<Campaign> {
        val campaignList = queryFactory
                .selectFrom(c)
                .orderBy(c.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
        val count = count()

        return PageImpl(campaignList, pageable, count)
    }

    override fun findCampaignLinksOrderByClicks(campaignId: Long): List<LinkDto> {
        val query = getNamedQuery("@GET_LINKS_BY_CAMPAIGN_ID_ORDER_BY_CLICKS")
        query.setParameter("id", campaignId)
        val result = query.resultList
        return result.stream()
                .map { objects ->
                    val o = objects as Array<*>
                    val id = (o[0] as BigInteger).toLong()
                    val cid = (o[1] as BigInteger).toLong()
                    val name = o[2] as String
                    val uid = o[3] as String
                    val url = o[4] as String
                    val createdAt = (o[5] as Timestamp).toLocalDateTime()
                    val validClicks = (o[6] as BigInteger).toInt()
                    val linkType = o[7] as String

                    val dto = LinkDto()
                    dto.id = id
                    dto.campaignId = cid
                    dto.name = name
                    dto.uid = uid
                    dto.url = url
                    dto.createdAt = createdAt
                    dto.validClicks = validClicks.toLong()
                    dto.linkType = linkType
                    if (linkType == LinkType.Banner.toString() && o[8] != null) {
                        val bannerId = (o[8] as BigInteger).toLong()
                        val bannerType = o[9] as String
                        val bgColor = o[10] as String
                        val imageUrl = o[11] as String
                        val internalUrl = o[12] as String
                        val isCover = o[13] as Boolean
                        val title = o[14] as String
                        val subtitle = o[15] as String

                        val banner = BannerDto()
                        banner.id = bannerId
                        banner.bannerType = bannerType
                        banner.bgColor = bgColor
                        banner.internalUrl = internalUrl
                        banner.imageUrl = imageUrl
                        banner.isCover = isCover
                        banner.title = title
                        banner.subtitle = subtitle
                        dto.banner = banner
                    }

                    dto
                }.collect(Collectors.toList())
    }

    override fun getUniqueVisitorGroupByOs(campaignId: Long): List<ShareStat> {
        return getUniqueVisitorGroupBy(campaignId, QVisitor.visitor.os)
    }

    override fun getUniqueVisitorGroupByBrowser(campaignId: Long): List<ShareStat> {
        return getUniqueVisitorGroupBy(campaignId, QVisitor.visitor.browser)
    }

    override fun getTopCampaigns(start: LocalDateTime, end: LocalDateTime, clientName: String, limit: Int): List<ShareStat> {
        val l = QLink.link
        val cl = QClick.click
        val alias = Expressions.numberPath(Long::class.java, "statValue")
        val results = queryFactory
                .select(c.name, cl.visitorId.countDistinct().`as`(alias))
                .from(cl)
                .innerJoin(l).on(l.id.eq(cl.linkId))
                .innerJoin(c).on(c.id.eq(l.campaignId))
                .where(c.clientName.eq(clientName)
                        .and(cl.timestamp.between(c.startAt, c.endAt))
                        .and(c.startAt.between(start, end))
                )
                .groupBy(c.id)
                .orderBy(alias.desc())
                .limit(limit.toLong())
                .fetch()
        return results.stream().map { tuple ->
            var statName = "No name"
            var statValue = 0L
            tuple.get(0, String::class.java)?.let {
                statName = it
            }
            tuple.get(1, Long::class.java)?.let {
                statValue = it
            }
            ShareStat(statName, statValue)
        }.collect(Collectors.toList())
    }

    override fun getUniqueVisitorTrendHourly(campaignId: Long): List<TimeDomainStat> {
        val query = getNamedQuery("@GET_UV_TREND_HOURLY_BY_HOUR_BY_CAMPAIGN_ID")
        query.setParameter("id", campaignId)
        val result = query.resultList
        return result.stream()
                .map { objects ->
                    val o = objects as Array<*>
                    val statDateTime = (o[0] as Timestamp).toLocalDateTime()
                    val statValue = (o[1] as BigInteger).toLong()

                    TimeDomainStat(statDateTime, statValue)
                }.collect(Collectors.toList())
    }

    override fun findBannerOnCampaignPeriodByPriority(): List<Banner> {
        val l = QLink.link
        val b = QBanner.banner
        val now = LocalDateTime.now()

        return queryFactory
                .selectFrom(b)
                .leftJoin(l).on(b.linkId.eq(l.id))
                .leftJoin(c).on(l.campaignId.eq(c.id))
                .where(c.startAt.loe(now).and(c.endAt.goe(now)).or(b.linkId.isNull))
                .orderBy(b.priority.asc())
                .fetch()
    }

    override fun getRawDataReport(campaignId: Long): List<RawDataReportRow> {
        val l = QLink.link
        val cl = QClick.click
        val v = QVisitor.visitor
        val results = queryFactory
                .select(c.name, l.name, l.url, v.device, v.os, v.osVersion, v.browser, v.browserVersion, v.ipAddress, cl.timestamp)
                .from(cl)
                .innerJoin(v).on(cl.visitorId.eq(v.id))
                .innerJoin(l).on(l.id.eq(cl.linkId))
                .innerJoin(c).on(c.id.eq(l.campaignId))
                .where(c.id.eq(campaignId)
                        .and(cl.timestamp.between(c.startAt, c.endAt))
                )
                .orderBy(cl.timestamp.desc())
                .fetch()

        return results.stream().map { tuple ->
            val campaignName = tuple.get(0, String::class.java)
            val linkName = tuple.get(1, String::class.java)
            val linkTo = tuple.get(2, String::class.java)
            val device = tuple.get(3, String::class.java)
            val os = tuple.get(4, String::class.java)
            val osVersion = tuple.get(5, String::class.java)
            val browser = tuple.get(6, String::class.java)
            val browserVersion = tuple.get(7, String::class.java)
            val ipAddress = tuple.get(8, String::class.java)
            val timestamp = tuple.get(9, LocalDateTime::class.java)

            val reportRow = RawDataReportRow()
            reportRow.campaignName = campaignName
            reportRow.linkName = linkName
            reportRow.linkTo = linkTo
            reportRow.device = device
            reportRow.os = os
            reportRow.osVersion = osVersion
            reportRow.browser = browser
            reportRow.browserVersion = browserVersion
            reportRow.ipAddress = ipAddress
            reportRow.timestamp = timestamp?.format(DateTimeFormatter.ISO_DATE_TIME)
            reportRow
        }.collect(Collectors.toList())
    }

    private fun getUniqueVisitorGroupBy(campaignId: Long?, path: StringPath): List<ShareStat> {
        val qVisitor = QVisitor.visitor
        val qLink = QLink.link
        val qClick = QClick.click
        val alias = Expressions.numberPath(Long::class.java, "statValue")
        val results = queryFactory
                .select(path, qVisitor.id.countDistinct().`as`(alias))
                .from(qClick)
                .innerJoin(qVisitor).on(qVisitor.id.eq(qClick.visitorId))
                .innerJoin(qLink).on(qLink.id.eq(qClick.linkId))
                .innerJoin(c).on(c.id.eq(qLink.campaignId))
                .where(c.id.eq(campaignId)
                        .and(qVisitor.timestamp.between(c.startAt, c.endAt)))
                .groupBy(path)
                .orderBy(alias.desc())
                .fetch()
        return results.stream().map { row ->
            var statName = "No name"
            var statValue = 0L
            row.get(0, String::class.java)?.let {
                statName = it
            }
            row.get(1, Long::class.java)?.let {
                statValue = it
            }
            ShareStat(statName, statValue)
        }.collect(Collectors.toList())
    }

    private fun getNamedQuery(queryName: String): Query {
        return entityManager.unwrap(Session::class.java).getNamedQuery(queryName)
    }

}
