package com.indiepost.repository.jpa;

import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.model.User;
import com.indiepost.model.analytics.QVisitor;
import com.indiepost.model.analytics.Visitor;
import com.indiepost.repository.VisitorRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.repository.utils.ResultMapper.toShareStateList;
import static com.indiepost.repository.utils.ResultMapper.toTimeDomainStatList;
import static com.indiepost.utils.DateUtil.normalizeTimeDomainStats;

/**
 * Created by jake on 8/9/17.
 */
@Repository
public class VisitorRepositoryJpa implements VisitorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QVisitor qVisitor = QVisitor.visitor;

    @Override
    public Long save(Visitor visitor) {
        if (visitor.getUserId() != null) {
            User userReference = entityManager.getReference(User.class, visitor.getUserId());
            visitor.setUser(userReference);
        }
        entityManager.persist(visitor);
        return visitor.getId();
    }

    @Override
    public void delete(Visitor visitor) {
        entityManager.remove(visitor);
    }

    @Override
    public Visitor findOne(Long id) {
        return entityManager.find(Visitor.class, id);
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until) {
        return getQueryFactory().selectFrom(qVisitor)
                .where(qVisitor.timestamp.between(since, until)
                        .and(qVisitor.isAdVisitor.isFalse()))
                .fetchCount();
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until, String client) {
        return getQueryFactory().selectFrom(qVisitor)
                .where(qVisitor.timestamp.between(since, until)
                        .and(qVisitor.appName.eq(client)))
                .fetchCount();
    }

    @Override
    public List<TimeDomainStat> getVisitorTrend(LocalDateTime since, LocalDateTime until, TimeDomainDuration duration) {
        switch (duration) {
            case HOURLY:
                return getVisitorTrendHourly(since, until);
            case DAILY:
                return getVisitorTrendDaily(since, until);
            case MONTHLY:
                return getVisitorTrendMonthly(since, until);
            case YEARLY:
                return getVisitorTrendYearly(since, until);
            default:
                return getVisitorTrendHourly(since, until);
        }
    }

    private List<TimeDomainStat> getVisitorTrendHourly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_HOURLY");
        List<TimeDomainStat> trend = toTimeDomainStatList(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate());
    }

    private List<TimeDomainStat> getVisitorTrendDaily(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_DAILY");
        return toTimeDomainStatList(query, TimeDomainDuration.DAILY, since, until);
    }

    private List<TimeDomainStat> getVisitorTrendMonthly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_MONTHLY");
        return toTimeDomainStatList(query, TimeDomainDuration.MONTHLY, since, until);
    }

    private List<TimeDomainStat> getVisitorTrendYearly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_YEARLY");
        return toTimeDomainStatList(query, TimeDomainDuration.YEARLY, since, until);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Integer limit) {
        Query query = getNamedQuery("@GET_TOP_REFERRERS");
        return toShareStateList(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Integer limit, String client) {
        Query query = getNamedQuery("@GET_TOP_REFERRERS_BY_CLIENT");
        return toShareStateList(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Integer limit) {
        Query query = getNamedQuery("@GET_TOP_WEB_BROWSERS");
        return toShareStateList(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Integer limit, String client) {
        Query query = getNamedQuery("@GET_TOP_WEB_BROWSERS_BY_CLIENT");
        return toShareStateList(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Integer limit) {
        Query query = getNamedQuery("@GET_TOP_OS");
        return toShareStateList(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Integer limit, String client) {
        Query query = getNamedQuery("@GET_TOP_OS_BY_CLIENT");
        return toShareStateList(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Integer limit) {
        Query query = getNamedQuery("@GET_TOP_CHANNELS");
        return toShareStateList(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Integer limit, String client) {
        Query query = getNamedQuery("@GET_TOP_CHANNELS_BY_CLIENT");
        return toShareStateList(query, since, until, limit, client);
    }

    private Query getNamedQuery(String queryName) {
        return entityManager.unwrap(Session.class).getNamedQuery(queryName);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
