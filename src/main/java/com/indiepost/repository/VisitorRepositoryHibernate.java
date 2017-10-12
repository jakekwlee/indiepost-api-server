package com.indiepost.repository;

import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.model.analytics.Visitor;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.getShare;
import static com.indiepost.repository.utils.CriteriaUtils.getTrend;
import static com.indiepost.utils.DateUtil.normalizeTimeDomainStats;

/**
 * Created by jake on 8/9/17.
 */
@SuppressWarnings("JpaQueryApiInspection")
@Repository
public class VisitorRepositoryHibernate implements VisitorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Visitor visitor) {
        return (Long) getSession().save(visitor);
    }

    @Override
    public void update(Visitor visitor) {
        getSession().update(visitor);
    }

    @Override
    public void delete(Visitor visitor) {
        getSession().delete(visitor);
    }

    @Override
    public Visitor findOne(Long id) {
        return entityManager.find(Visitor.class, id);
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.between("v.timestamp", since, until));
        criteria.add(Restrictions.eq("v.adVisitor", false));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until, String client) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.between("v.timestamp", since, until));
        criteria.add(Restrictions.eq("v.appName", client));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
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
        List<TimeDomainStat> trend = getTrend(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate());
    }

    private List<TimeDomainStat> getVisitorTrendDaily(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_DAILY");
        return getTrend(query, TimeDomainDuration.DAILY, since, until);
    }

    private List<TimeDomainStat> getVisitorTrendMonthly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_MONTHLY");
        return getTrend(query, TimeDomainDuration.MONTHLY, since, until);
    }

    private List<TimeDomainStat> getVisitorTrendYearly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_VISITORS_TREND_YEARLY");
        return getTrend(query, TimeDomainDuration.YEARLY, since, until);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_REFERRERS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_REFERRERS_BY_CLIENT");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_WEB_BROWSERS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_WEB_BROWSERS_BY_CLIENT");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_OS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_OS_BY_CLIENT");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_CHANNELS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_CHANNELS_BY_CLIENT");
        return getShare(query, since, until, limit, client);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Query getNamedQuery(String queryName) {
        return getSession().getNamedQuery(queryName);
    }

    private Criteria createCriteria() {
        return getSession().createCriteria(Visitor.class, "v");
    }
}
