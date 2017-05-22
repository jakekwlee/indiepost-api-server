package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;
import com.indiepost.model.Visitor;
import com.indiepost.utils.DateUtils;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
@Repository
@SuppressWarnings("unchecked")
public class StatRepositoryNativeSql implements StatRepository {

    private static final String EXCLUDE_GOOGLE_BOT = "AND v.browser <> 'Googlebot' AND v.browser <> 'Mediapartners-Google' ";

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
    public Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until) {
        String sqlQuery = "SELECT count(DISTINCT s.path, v.id) FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :s AND :u " +
                EXCLUDE_GOOGLE_BOT;
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until) {
        String sqlQuery = "SELECT count(DISTINCT s.postId, v.id) FROM Stats s " +
                "INNER JOIN Visitors v ON s.visitorId = v.id " +
                "WHERE s.timestamp BETWEEN :s AND :u " +
                "AND s.postId IS NOT NULL " +
                EXCLUDE_GOOGLE_BOT;
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalPostviews(LocalDateTime since, LocalDateTime until) {
        return getTotalPageviews(since, until, StatType.POST);
    }

    @Override
    public Long getTotalPageviews(LocalDateTime since, LocalDateTime until, StatType type) {
        Criteria criteria = getSession().createCriteria(Stat.class);
        criteria.createAlias("visitor", "v");
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        criteria.add(Restrictions.ne("v.browser", "Mediapartners-Google"));
        setParameterCriteria(criteria, since, until);
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until) {
        return getTotalVisitors(since, until, null);
    }

    @Override
    public Long getTotalVisitors(LocalDateTime since, LocalDateTime until, ClientType appName) {
        Criteria criteria = getSession().createCriteria(Visitor.class);
        criteria.add(Restrictions.ne("browser", "Googlebot"));
        criteria.add(Restrictions.ne("browser", "Mediapartners-Google"));
        criteria.add(Restrictions.between("timestamp", since, until));
        if (appName != null) {
            criteria.add(Restrictions.eq("appName", appName));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public List<TimeDomainStat> getPageviewTrend(LocalDateTime since, LocalDateTime until) {
        String sqlQuery;
        Duration duration = Duration.between(since, until);

        if (since.getYear() != until.getYear()) {
            sqlQuery = "SELECT makedate(year(s.timestamp), 1) AS statDateTime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY year(s.timestamp) " +
                    "ORDER BY statDateTime";
        } else if (since.getMonthValue() != until.getMonthValue()) {
            sqlQuery = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDateTime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY year(s.timestamp), month(s.timestamp) " +
                    "ORDER BY statDateTime";
        } else if (duration.toHours() > 48) {
            sqlQuery = "SELECT date(s.timestamp) AS statDateTime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY date(s.timestamp) " +
                    "ORDER BY statDateTime";
        } else {
            sqlQuery = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDateTime, count(*) AS statCount " +
                    "FROM Stats AS s " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY date(s.timestamp), hour(s.timestamp) " +
                    "ORDER BY statDateTime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return query.list();
    }

    @Override
    public List<TimeDomainStat> getVisitorTrend(LocalDateTime since, LocalDateTime until) {
        String sqlQuery;
        Duration duration = Duration.between(since, until);

        if (since.getYear() != until.getYear()) {
            sqlQuery = "SELECT makedate(year(v.timestamp), 1) AS statDateTime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY year(v.timestamp) " +
                    "ORDER BY statDateTime";
        } else if (since.getMonthValue() != until.getMonthValue()) {
            sqlQuery = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDateTime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY year(v.timestamp), month(v.timestamp) " +
                    "ORDER BY statDateTime";
        } else if (duration.toHours() > 48) {
            sqlQuery = "SELECT date(v.timestamp) AS statDateTime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY date(v.timestamp) " +
                    "ORDER BY statDateTime";
        } else {
            sqlQuery = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDateTime, count(*) AS statCount " +
                    "FROM Visitors AS v " +
                    "WHERE v.timestamp BETWEEN :s AND :u " +
                    EXCLUDE_GOOGLE_BOT +
                    "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                    "ORDER BY statDateTime";
        }

        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until) {
        String sqlQuery =
                "SELECT c.name AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY c.name " +
                        "ORDER BY statCount DESC";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until) {
        String sqlQuery =
                "SELECT p.displayName AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY p.displayName " +
                        "ORDER BY statCount DESC";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "AND v.appName = :t " +
                        "GROUP BY s.path " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY p.id " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
        String sqlQuery =
                "SELECT p.title AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "AND s.type = :st " +
                        "AND v.appName = :t " +
                        "GROUP BY p.id " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<PostStat> getPostsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT p.id AS id, p.title AS title, p.displayName AS author, c.name AS category, count(*) AS pageview " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY p.id " +
                        "ORDER BY pageview DESC , p.id DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(PostStat.class));
        return query.list();
    }

    @Override
    public List<PostStat> getPostsOrderByUniquePageviews(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT s.postId AS id, count(DISTINCT v.id) AS uniquePageview " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.postId IS NOT NULL " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY s.postId " +
                        "ORDER BY uniquePageview DESC, s.postId DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(PostStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
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
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopLandingPosts(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
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
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getSecondaryViewedPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
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
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getSecondaryViewedPosts(LocalDateTime since, LocalDateTime until, Long limit, ClientType type) {
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
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setString("st", StatType.POST.toString());
        query.setString("t", type.toString());
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT s.referrer AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        "AND s.referrer IS NOT NULL " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY s.referrer " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT v.browser AS statName, count(*) AS statCount " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY v.browser " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT v.os AS statName, count(*) AS statCount " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY v.os " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT t.name AS statName, count(*) AS statCount FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Posts_Tags pt ON p.id = pt.postId " +
                        "INNER JOIN Tags t ON pt.tagId = t.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY t.id " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    @Override
    public List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit) {
        String sqlQuery =
                "SELECT s.channel AS statName, count(*) AS statCount " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :s AND :u " +
                        "AND s.isLandingPage IS TRUE " +
                        EXCLUDE_GOOGLE_BOT +
                        "GROUP BY s.channel " +
                        "ORDER BY statCount DESC " +
                        "LIMIT :l";
        Query query = getSession().createSQLQuery(sqlQuery);
        query.setParameter("s", DateUtils.localDateTimeToDate(since));
        query.setParameter("u", DateUtils.localDateTimeToDate(until));
        query.setLong("l", limit);
        query.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return query.list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private void setParameterCriteria(Criteria criteria, LocalDateTime since, LocalDateTime until) {
        criteria.add(Restrictions.between("timestamp", since, until));
    }
}
