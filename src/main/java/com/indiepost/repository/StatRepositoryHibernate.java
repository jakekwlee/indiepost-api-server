package com.indiepost.repository;

import com.indiepost.dto.stat.ShareStatResult;
import com.indiepost.dto.stat.TimeDomainStatResult;
import com.indiepost.enums.Types;
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
import java.math.BigInteger;
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
    public Long getTotalVisitors(Date since, Date until, Types.ClientType appName) {
        Criteria criteria = getSession().createCriteria(Visitor.class);
        setDateCriteria(criteria, since, until);
        criteria.add(Restrictions.eq("appName", appName));
        criteria.setProjection(Projections.rowCount());
        return ((BigInteger) criteria.uniqueResult()).longValue();
    }

    @Override
    public List<TimeDomainStatResult> getPageviewTrend(Date since, Date until, Period period) {
        String sqlQuery;
        if (period.getYears() > 0) {
            sqlQuery = "SELECT makedate(year(s.timestamp), 1) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                    "AND s.type <> 'ACTION' " +
                    "GROUP BY year(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getMonths() > 0) {
            sqlQuery = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                    "AND s.type <> 'ACTION' " +
                    "GROUP BY year(s.timestamp), month(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getDays() > 0) {
            sqlQuery = "SELECT date(s.timestamp) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                    "AND s.type <> 'ACTION' " +
                    "GROUP BY date(s.timestamp) " +
                    "ORDER BY statDatetime";
        } else {
            sqlQuery = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDatetime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                    "AND s.type <> 'ACTION' " +
                    "GROUP BY date(s.timestamp), hour(s.timestamp) " +
                    "ORDER BY statDatetime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", since);
        query.setParameter("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStatResult.class));

        return query.list();
    }

    @Override
    public List<TimeDomainStatResult> getVisitorTrend(Date since, Date until, Period period) {
        String sqlQuery;
        if (period.getYears() > 0) {
            sqlQuery = "SELECT makedate(year(v.timestamp), 1) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
                    "GROUP BY year(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getMonths() > 0) {
            sqlQuery = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
                    "GROUP BY year(v.timestamp), month(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else if (period.getDays() > 0) {
            sqlQuery = "SELECT date(v.timestamp) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
                    "GROUP BY date(v.timestamp) " +
                    "ORDER BY statDatetime";
        } else {
            sqlQuery = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDatetime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
                    "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                    "ORDER BY statDatetime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", since);
        query.setParameter("u", until);
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStatResult.class));

        return query.list();
    }

    @Override
    public List<ShareStatResult> getPageviewsByCategory(Date since, Date until) {
        String sqlQuery =
                "SELECT c.name AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
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
    public List<ShareStatResult> getMostViewedPages(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.type) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.type <> :a " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setString("a", StatType.ACTION.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getMostViewedPosts(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.type = :t " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setString("t", StatType.POST.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopLandingPages(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.type) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "GROUP BY s.path " +
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
    public List<ShareStatResult> getTopLandingPosts(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.type = :t " +
                        "AND s.isLandingPage IS TRUE " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setString("t", StatType.POST.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getSecondlyViewedPages(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.type) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.isLandingPage IS FALSE " +
                        "GROUP BY s.path " +
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
    public List<ShareStatResult> getSecondlyViewedPosts(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.type = :t " +
                        "AND s.isLandingPage IS FALSE " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setTimestamp("s", since);
        query.setTimestamp("u", until);
        query.setLong("l", limit);
        query.setString("t", StatType.POST.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStatResult.class));
        return query.list();
    }

    @Override
    public List<ShareStatResult> getTopReferrers(Date since, Date until, Long limit) {
        String sqlQuery =
                "SELECT s.referrer AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "AND s.referrer IS NOT NULL " +
                        "GROUP BY s.path " +
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
                        "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
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
                        "WHERE v.timestamp >= :s AND v.timestamp <= :u " +
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
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
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
                        "WHERE s.timestamp >= :s AND s.timestamp <= :u " +
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
