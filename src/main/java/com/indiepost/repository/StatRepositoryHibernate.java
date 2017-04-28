package com.indiepost.repository;

import com.indiepost.dto.stat.ShareStatResult;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;
import com.indiepost.model.Visitor;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Period;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
@Repository
public class StatRepositoryHibernate implements StatRepository {

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
    public Long getTotalPageviews(Date since, Date until) {
        return getTotalPageviews(since, until, null);
    }

    @Override
    public Long getTotalPageviews(Date since, Date until, StatType type) {
        Criteria criteria = getSession().createCriteria(Stat.class);
        setDateCriteria(criteria, since, until);
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));
        } else {
            criteria.add(Restrictions.ne("type", StatType.ACTION));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalVisitors(Date since, Date until) {
        Criteria criteria = getSession().createCriteria(Visitor.class);
        setDateCriteria(criteria, since, until);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalVisitors(Date since, Date until, ClientType appName) {
        Criteria criteria = getSession().createCriteria(Visitor.class);
        setDateCriteria(criteria, since, until);
        criteria.add(Restrictions.eq("appName", appName));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public List<TimeDomainStat> getPageviewTrend(Date since, Date until, Period period) {
        String sqlQuery;
        if (period.getYears() > 0) {
            sqlQuery = "SELECT makedate(year(s.timestamp), 1) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    "GROUP BY year(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getMonths() > 0) {
            sqlQuery = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    "GROUP BY year(s.timestamp), month(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getDays() > 0) {
            sqlQuery = "SELECT date(s.timestamp) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    "GROUP BY date(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else {
            sqlQuery = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    "GROUP BY date(s.timestamp), hour(s.timestamp) " +
                    "ORDER BY statDatetime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", since);
        query.setParameter("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return query.list();
    }

    @Override
    public List<TimeDomainStat> getVisitorTrend(Date since, Date until, Period period) {
        String sqlQuery;
        if (period.getYears() > 0) {
            sqlQuery = "SELECT makedate(year(v.timestamp), 1) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    "GROUP BY year(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getMonths() > 0) {
            sqlQuery = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    "GROUP BY year(v.timestamp), month(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getDays() > 0) {
            sqlQuery = "SELECT date(v.timestamp) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    "GROUP BY date(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else {
            sqlQuery = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                    "ORDER BY statDatetime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", since);
        query.setParameter("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getPageviewsByCategory(Date since, Date until) {
        String sqlQuery =
                "SELECT c.name AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "GROUP BY c.name " +
                        "ORDER BY statCount DESC";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getPageviewByAuthor(Date since, Date until) {
        String sqlQuery =
                "SELECT p.displayName AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "GROUP BY p.displayName " +
                        "ORDER BY statCount DESC";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopPages(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND v.appName = :t " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopPosts(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.type = :st " +
                        "AND v.appName = :t " +
                        "GROUP BY p.id " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopLandingPages(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "AND v.appName = :t " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopLandingPosts(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "AND s.type = :st " +
                        "AND v.appName = :t " +
                        "GROUP BY p.title " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getSecondaryViewedPages(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND v.appName = :t " +
                        "AND s.isLandingPage IS FALSE " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getSecondaryViewedPosts(Date since, Date until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.type = :st " +
                        "AND s.isLandingPage IS FALSE " +
                        "AND v.appName = :t " +
                        "GROUP BY p.title " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopReferrers(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT s.referrer AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "AND s.referrer IS NOT NULL " +
                        "GROUP BY s.referrer " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopWebBrowsers(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT v.browser AS statName, count(*) AS statCount " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :s AND :u " +
                        "GROUP BY v.browser " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopOs(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT v.os AS statName, count(*) AS statCount " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :s AND :u " +
                        "GROUP BY v.os " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopTags(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT t.name AS statName, count(*) AS statCount FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Posts_Tags pt ON p.id = pt.postId " +
                        "INNER JOIN Tags t ON pt.tagId = t.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "GROUP BY t.id " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopChannel(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT s.channel AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "GROUP BY s.channel " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private void setDateCriteria(Criteria criteria, Date since, Date until) {
        criteria.add(Restrictions.between("timestamp", since, until));
    }
}
