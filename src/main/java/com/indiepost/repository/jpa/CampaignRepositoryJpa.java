package com.indiepost.repository.jpa;

import com.indiepost.dto.stat.LinkDto;
import com.indiepost.dto.stat.RawDataReportRow;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.model.analytics.*;
import com.indiepost.repository.CampaignRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CampaignRepositoryJpa implements CampaignRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QCampaign c = QCampaign.campaign;

    @Override
    public void save(Campaign campaign) {
        entityManager.persist(campaign);
    }

    @Override
    public Optional<Campaign> findOne(Long id) {
        Campaign campaign = entityManager.find(Campaign.class, id);
        if (campaign != null) {
            return Optional.of(campaign);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        Campaign campaignReference = entityManager.getReference(Campaign.class, id);
        entityManager.remove(campaignReference);
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(c)
                .fetchCount();
    }

    @Override
    public Page<Campaign> find(Pageable pageable) {
        List<Campaign> campaignList = getQueryFactory()
                .selectFrom(c)
                .orderBy(c.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = count();

        return new PageImpl<>(campaignList, pageable, count);
    }

    @Override
    public List<LinkDto> findCampaignLinksOrderByClicks(Long campaignId) {
        Query query = getNamedQuery("@GET_LINKS_BY_CAMPAIGN_ID_ORDER_BY_CLICKS");
        query.setParameter("id", campaignId);
        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(objects -> {
                    Long id = ((BigInteger) objects[0]).longValue();
                    Long cid = ((BigInteger) objects[1]).longValue();
                    String name = (String) objects[2];
                    String uid = (String) objects[3];
                    String url = (String) objects[4];
                    LocalDateTime createdAt = ((Timestamp) objects[5]).toLocalDateTime();
                    Integer validClicks = ((BigInteger) objects[6]).intValue();

                    LinkDto dto = new LinkDto();
                    dto.setId(id);
                    dto.setCampaignId(cid);
                    dto.setName(name);
                    dto.setUid(uid);
                    dto.setUrl(url);
                    dto.setCreatedAt(createdAt);
                    dto.setValidClicks(validClicks.longValue());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ShareStat> getUniqueVisitorGroupByOs(Long campaignId) {
        return getUniqueVisitorGroupBy(campaignId, QVisitor.visitor.os);
    }

    @Override
    public List<ShareStat> getUniqueVisitorGroupByBrowser(Long campaignId) {
        return getUniqueVisitorGroupBy(campaignId, QVisitor.visitor.browser);
    }

    @Override
    public List<ShareStat> getTopCampaigns(LocalDateTime start, LocalDateTime end, String clientName, int limit) {
        QLink l = QLink.link;
        QClick cl = QClick.click;
        NumberPath<Long> alias = Expressions.numberPath(Long.class, "statValue");
        List<Tuple> results = getQueryFactory()
                .select(c.name, cl.visitorId.countDistinct().as(alias))
                .from(cl)
                .innerJoin(l).on(l.id.eq(cl.linkId))
                .innerJoin(c).on(c.id.eq(l.campaignId))
                .where(c.clientName.eq(clientName)
                        .and(cl.timestamp.between(c.startAt, c.endAt))
                        .and(c.startAt.between(start, end))
                )
                .groupBy(c.id)
                .orderBy(alias.desc())
                .limit(limit)
                .fetch();
        return results.stream().map(tuple -> {
            String statName = tuple.get(0, String.class);
            Long statValue = tuple.get(1, Long.class);
            return new ShareStat(statName, statValue);
        }).collect(Collectors.toList());
    }

    @Override
    public List<TimeDomainStat> getUniqueVisitorTrendHourly(Long campaignId) {
        Query query = getNamedQuery("@GET_UV_TREND_HOURLY_BY_HOUR_BY_CAMPAIGN_ID");
        query.setParameter("id", campaignId);
        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(objects -> {
                    LocalDateTime statDateTime = ((Timestamp) objects[0]).toLocalDateTime();
                    Long statValue = ((BigInteger) objects[1]).longValue();

                    return new TimeDomainStat(statDateTime, statValue);
                }).collect(Collectors.toList());
    }

    @Override
    public List<RawDataReportRow> getRawDataReport(Long campaignId) {
        QLink l = QLink.link;
        QClick cl = QClick.click;
        QVisitor v = QVisitor.visitor;
        List<Tuple> results = getQueryFactory()
                .select(c.name, l.name, l.url, v.device, v.os, v.osVersion, v.browser, v.browserVersion, v.ipAddress, cl.timestamp)
                .from(cl)
                .innerJoin(v).on(cl.visitorId.eq(v.id))
                .innerJoin(l).on(l.id.eq(cl.linkId))
                .innerJoin(c).on(c.id.eq(l.campaignId))
                .where(c.id.eq(campaignId)
                        .and(cl.timestamp.between(c.startAt, c.endAt))
                )
                .orderBy(cl.timestamp.desc())
                .fetch();

        return results.stream().map(tuple -> {
            String campaignName = tuple.get(0, String.class);
            String linkName = tuple.get(1, String.class);
            String linkTo = tuple.get(2, String.class);
            String device = tuple.get(3, String.class);
            String os = tuple.get(4, String.class);
            String osVersion = tuple.get(5, String.class);
            String browser = tuple.get(6, String.class);
            String browserVersion = tuple.get(7, String.class);
            String ipAddress = tuple.get(8, String.class);
            LocalDateTime timestamp = tuple.get(9, LocalDateTime.class);

            RawDataReportRow reportRow = new RawDataReportRow();
            reportRow.setCampaignName(campaignName);
            reportRow.setLinkName(linkName);
            reportRow.setLinkTo(linkTo);
            reportRow.setDevice(device);
            reportRow.setOs(os);
            reportRow.setOsVersion(osVersion);
            reportRow.setBrowser(browser);
            reportRow.setBrowserVersion(browserVersion);
            reportRow.setIpAddress(ipAddress);
            reportRow.setTimestamp(timestamp.format(DateTimeFormatter.ISO_DATE_TIME));
            return reportRow;
        }).collect(Collectors.toList());
    }

    private List<ShareStat> getUniqueVisitorGroupBy(Long campaignId, StringPath path) {
        QVisitor qVisitor = QVisitor.visitor;
        QLink qLink = QLink.link;
        QClick qClick = QClick.click;
        NumberPath<Long> alias = Expressions.numberPath(Long.class, "statValue");
        List<Tuple> results = getQueryFactory()
                .select(path, qVisitor.id.countDistinct().as(alias))
                .from(qClick)
                .innerJoin(qVisitor).on(qVisitor.id.eq(qClick.visitorId))
                .innerJoin(qLink).on(qLink.id.eq(qClick.linkId))
                .innerJoin(c).on(c.id.eq(qLink.campaignId))
                .where(c.id.eq(campaignId)
                        .and(qVisitor.timestamp.between(c.startAt, c.endAt)))
                .groupBy(path)
                .orderBy(alias.desc())
                .fetch();
        return results.stream().map(row -> {
            String statName = row.get(0, String.class);
            Long statValue = row.get(1, Long.class);
            return new ShareStat(statName, statValue);
        }).collect(Collectors.toList());
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    private Query getNamedQuery(String queryName) {
        return entityManager.unwrap(Session.class).getNamedQuery(queryName);
    }

}
