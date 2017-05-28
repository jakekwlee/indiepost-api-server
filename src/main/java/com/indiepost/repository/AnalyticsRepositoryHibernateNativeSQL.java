package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.model.Stat;
import com.indiepost.model.Visitor;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.utils.DateUtils.localDateTimeToDate;
import static com.indiepost.utils.DateUtils.normalizeTimeDomainStats;

/**
 * Created by jake on 17. 5. 24.
 */
@Repository
public class AnalyticsRepositoryHibernateNativeSQL implements AnalyticsRepository {
    private static final String EXCLUDE_BOTS_RESTRICTION = "AND v.browser <> 'Googlebot' AND v.browser <> 'Mediapartners-Google' ";
    private static final String CLIENT_TYPE_RESTRICTION = "AND v.appName = :client ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Stat stat) {
        return (Long) getSession().save(stat);
    }

    @Override
    public void delete(Stat stat) {
        getSession().delete(stat);
    }

    @Override
    public Stat findById(Long id) {
        return entityManager.find(Stat.class, id);
    }

    @Override
    public void update(Stat stat) {
        getSession().update(stat);
    }

    @Override
    public Long getTotalPageviews(LocalDateTime since, LocalDateTime until) {
        return getTotalPageviews(since, until, null);
    }

    @Override
    public Long getTotalPageviews(LocalDateTime since, LocalDateTime until, StatType statType) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("visitor", "v");
        if (statType != null) {
            criteria.add(Restrictions.eq("s.type", statType));
        }
        criteria.add(Restrictions.between("s.timestamp", since, until));
        setExcludeBots(criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalPostviews(LocalDateTime since, LocalDateTime until) {
        return getTotalPageviews(since, until, StatType.POST);
    }

    @Override
    public Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until) {
        return getTotalUniquePageviews(since, until, null);
    }

    @Override
    public Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until, ClientType clientType) {
        String queryString = "SELECT count(DISTINCT s.path, v.id) FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(clientType);
        Query query = getSession().createSQLQuery(queryString);
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        if (clientType != null) {
            query.setParameter("client", clientType.toString());
        }
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until) {
        return getTotalUniquePostviews(since, until, null);
    }

    @Override
    public Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until, ClientType clientType) {
        String queryString = "SELECT count(DISTINCT s.postId, v.id) FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                "AND s.postId IS NOT NULL " +
                getClientTypeRestriction(clientType);
        Query query = getSession().createSQLQuery(queryString);
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        if (clientType != null) {
            query.setString("client", clientType.toString());
        }
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until) {
        return getTotalVisitors(since, until, null);
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until, ClientType clientType) {
        Criteria criteria = getSession().createCriteria(Visitor.class, "v");
        setExcludeBots(criteria);
        criteria.add(Restrictions.between("v.timestamp", since, until));
        if (clientType != null) {
            criteria.add(Restrictions.eq("v.appName", clientType));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public List<TimeDomainStat> getHourlyPageviewTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDateTime, count(*) AS statValue " +
                "FROM Stats AS s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY date(s.timestamp), hour(s.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        List<TimeDomainStat> trend = getTrend(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate());
    }

    @Override
    public List<TimeDomainStat> getDailyPageviewTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date(s.timestamp) AS statDateTime, count(*) AS statValue " +
                "FROM Stats AS s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY date(s.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.DAILY, since, until);
    }

    @Override
    public List<TimeDomainStat> getMonthlyPageviewTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDateTime, count(*) AS statValue " +
                "FROM Stats AS s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY year(s.timestamp), month(s.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.MONTHLY, since, until);
    }

    @Override
    public List<TimeDomainStat> getYearlyPageviewTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT makedate(year(s.timestamp), 1) AS statDateTime, count(*) AS statValue " +
                "FROM Stats AS s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY year(s.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.YEARLY, since, until);
    }

    @Override
    public List<TimeDomainStat> getHourlyVisitorTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDateTime, count(*) AS statValue " +
                "FROM Visitors AS v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        List<TimeDomainStat> trend = getTrend(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate());
    }

    @Override
    public List<TimeDomainStat> getDailyVisitorTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date(v.timestamp) AS statDateTime, count(*) AS statValue " +
                "FROM Visitors AS v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY date(v.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.DAILY, since, until);
    }

    @Override
    public List<TimeDomainStat> getMonthlyVisitorTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDateTime, count(*) AS statValue " +
                "FROM Visitors AS v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY year(v.timestamp), month(v.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.MONTHLY, since, until);
    }

    @Override
    public List<TimeDomainStat> getYearlyVisitorTrend(LocalDateTime since, LocalDateTime until) {
        String queryString = "SELECT makedate(year(v.timestamp), 1) AS statDateTime, count(*) AS statValue " +
                "FROM Visitors AS v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY year(v.timestamp) " +
                "ORDER BY statDateTime";
        Query query = getSession().createSQLQuery(queryString);
        return getTrend(query, TimeDomainDuration.YEARLY, since, until);
    }

    @Override
    public List<PostStat> getPostsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit) {
        String queryString = "SELECT p.id AS id, p.title AS title, p.displayName AS author, c.name AS category, count(*) AS pageview " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "INNER JOIN Posts p ON s.postId = p.id " +
                "INNER JOIN Categories c ON p.categoryId = c.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY p.id " +
                "ORDER BY pageview DESC , p.id DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getPostShare(query, since, until, limit);
    }

    @Override
    public List<PostStat> getPostsOrderByUniquePageviews(LocalDateTime since, LocalDateTime until, Long limit) {
        String queryString = "SELECT s.postId AS id, count(DISTINCT v.id) AS uniquePageview " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                "AND s.postId IS NOT NULL " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY s.postId " +
                "ORDER BY uniquePageview DESC, s.postId DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getPostShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until, Long limit) {
        String queryString = "SELECT p.displayName AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "INNER JOIN Posts p ON s.postId = p.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY p.displayName " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until, Long limit) {
        String queryString = "SELECT c.name AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "INNER JOIN Posts p ON s.postId = p.id " +
                "INNER JOIN Categories c ON p.categoryId = c.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                EXCLUDE_BOTS_RESTRICTION +
                "GROUP BY c.name " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopPages(since, until, limit, ClientType.INDIEPOST_WEBAPP);
    }

    @Override
    public List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "LEFT JOIN Posts p ON s.postId = p.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(client) +
                "GROUP BY s.path " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopPosts(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT p.title AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Posts p ON s.postId = p.id " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(client) +
                "GROUP BY p.id " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopLandingPages(since, until, limit, ClientType.INDIEPOST_WEBAPP);
    }

    @Override
    public List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "LEFT JOIN Posts p ON s.postId = p.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                "AND s.isLandingPage IS TRUE " +
                getClientTypeRestriction(client) +
                "GROUP BY s.path " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopReferrers(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT s.referrer AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                "AND s.isLandingPage IS TRUE " +
                "AND s.referrer IS NOT NULL " +
                getClientTypeRestriction(client) +
                "GROUP BY s.referrer " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopWebBrowsers(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT v.browser AS statName, count(*) AS statValue " +
                "FROM Visitors v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(client) +
                "GROUP BY v.browser " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopOs(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT v.os AS statName, count(*) AS statValue " +
                "FROM Visitors v " +
                "WHERE v.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(client) +
                "GROUP BY v.os " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopTags(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT t.name AS statName, count(*) AS statValue FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "INNER JOIN Posts p ON s.postId = p.id " +
                "INNER JOIN Posts_Tags pt ON p.id = pt.postId " +
                "INNER JOIN Tags t ON pt.tagId = t.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                getClientTypeRestriction(client) +
                "GROUP BY t.id " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit) {
        return getTopChannel(since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit, ClientType client) {
        String queryString = "SELECT s.channel AS statName, count(*) AS statValue " +
                "FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :since AND :until " +
                "AND s.isLandingPage IS TRUE " +
                getClientTypeRestriction(client) +
                "GROUP BY s.channel " +
                "ORDER BY statValue DESC " +
                "LIMIT :limit";
        Query query = getSession().createSQLQuery(queryString);
        return getShare(query, since, until, limit, client);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private List<TimeDomainStat> getTrend(Query query, TimeDomainDuration duration, LocalDateTime since, LocalDateTime until) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                if (TimeDomainDuration.HOURLY.equals(duration)) {
                    return new TimeDomainStat(
                            ((Timestamp) tuple[0]).toLocalDateTime(),
                            ((BigInteger) tuple[1]).longValue()
                    );
                }
                return new TimeDomainStat(
                        ((Date) tuple[0]).toLocalDate().atStartOfDay(),
                        ((BigInteger) tuple[1]).longValue()
                );
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        return query.list();
    }

    private List<ShareStat> getShare(Query query, LocalDateTime since, LocalDateTime until, Long limit) {
        return getShare(query, since, until, limit, null);
    }

    private List<ShareStat> getShare(Query query, LocalDateTime since, LocalDateTime until, Long limit, ClientType clientType) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setLong("limit", limit);
        if (clientType != null) {
            query.setParameter("client", clientType.toString());
        }
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                return new ShareStat(
                        (String) tuple[0],
                        ((BigInteger) tuple[1]).longValue()
                );
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        return query.list();
    }

    private List<PostStat> getPostShare(Query query, LocalDateTime since, LocalDateTime until, Long limit) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setLong("limit", limit);
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                PostStat postStat = new PostStat();
                for (int i = 0; i < tuple.length; ++i) {
                    switch (aliases[i]) {
                        case "id":
                            postStat.setId(((BigInteger) tuple[i]).longValue());
                            break;
                        case "title":
                            postStat.setTitle((String) tuple[i]);
                            break;
                        case "author":
                            postStat.setAuthor((String) tuple[i]);
                            break;
                        case "category":
                            postStat.setCategory((String) tuple[i]);
                            break;
                        case "pageview":
                            postStat.setPageview(((BigInteger) tuple[i]).longValue());
                            break;
                        case "uniquePageview":
                            postStat.setUniquePageview(((BigInteger) tuple[i]).longValue());
                            break;
                    }
                }
                return postStat;
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        return query.list();
    }

    private String getClientTypeRestriction(ClientType clientType) {
        return (clientType != null) ? CLIENT_TYPE_RESTRICTION + EXCLUDE_BOTS_RESTRICTION : EXCLUDE_BOTS_RESTRICTION;
    }

    private void setExcludeBots(Criteria criteria) {
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        criteria.add(Restrictions.ne("v.browser", "Mediapartners-Google"));
    }


}