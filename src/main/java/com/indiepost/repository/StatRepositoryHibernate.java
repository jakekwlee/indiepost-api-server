package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types;
import com.indiepost.model.Stat;
import com.indiepost.model.Visitor;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Period;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 5. 5.
 */
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
    public Long getTotalUniquePageviews(Date since, Date until) {
        DetachedCriteria subQuery = DetachedCriteria.forClass(Stat.class, "s");
        subQuery.createAlias("s.visitor", "v");
        subQuery.add(Restrictions.ne("v.browser", "Googlebot"));
        subQuery.add(Restrictions.between("s.timestamp", since, until));
        subQuery.setProjection(
                Projections.projectionList()
                        .add(Projections.groupProperty("s.path"))
                        .add(Projections.groupProperty("s.visitorId"))
                        .add(Property.forName("s.id"))
        );
        Criteria criteria = getSession().createCriteria(Stat.class);
        criteria.setProjection(
                Projections.projectionList()
                        .add(Projections.rowCount())
        ).add(Subqueries.geAll("s.id", subQuery));
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalUniquePostviews(Date since, Date until) {
        DetachedCriteria subQuery = DetachedCriteria.forClass(Stat.class, "s");
        subQuery.createAlias("s.visitor", "v");
        subQuery.add(Restrictions.ne("v.browser", "Googlebot"));
        subQuery.add(Restrictions.between("s.timestamp", since, until));
        subQuery.add(Restrictions.isNotNull("s.postId"));
        subQuery.setProjection(
                Projections.projectionList()
                        .add(Projections.groupProperty("s.postId"))
                        .add(Projections.groupProperty("s.visitorId"))
                        .add(Property.forName("s.id"))
        );
        Criteria criteria = getSession().createCriteria(Stat.class);
        criteria.setProjection(
                Projections.projectionList()
                        .add(Projections.rowCount())
        ).add(Subqueries.geAll("s.id", subQuery));
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalPostviews(Date since, Date until) {
        return getTotalPageviews(since, until, Types.StatType.POST);
    }

    @Override
    public Long getTotalPageviews(Date since, Date until, Types.StatType type) {
        Criteria criteria = getSession().createCriteria(Stat.class);
        criteria.createAlias("s.visitor", "v");
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        setDateCriteria(criteria, since, until);
        if (type != null) {
            criteria.add(Restrictions.eq("type", type));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalVisitors(Date since, Date until) {
        return getTotalVisitors(since, until, null);
    }

    @Override
    public Long getTotalVisitors(Date since, Date until, Types.ClientType appName) {
        Criteria criteria = getSession().createCriteria(Visitor.class, "v");
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        criteria.add(Restrictions.between("v.timestamp", since, until));
        if (appName != null) {
            criteria.add(Restrictions.eq("v.appName", appName));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public List<PostStat> getPostsOrderByPageviews(Date since, Date until, Long limit) {
        return null;
    }

    @Override
    public List<PostStat> getPostsOrderByUniquePageviews(Date since, Date until, Long limit) {
        return null;
    }

    @Override
    public List<TimeDomainStat> getPageviewTrend(Date since, Date until, Period period) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        criteria.addOrder(Order.asc("statDatetime"));
        ProjectionList projectionList = Projections.projectionList();
        String[] columnAliases = new String[]{"statDatetime", "statCount"};
        Type[] types = new Type[]{new DateType(), new LongType()};
        if (period.getYears() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "makedate(year(s.timestamp), 1) as statDatetime, count(*) as statCount",
                    "year(s.timestamp)", columnAliases, types
            ));
        } else if (period.getMonths() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount",
                    "year(s.timestamp), month(s.timestamp)", columnAliases, types
            ));
        } else if (period.getDays() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "date(s.timestamp) AS statDatetime, count(*) AS statCount",
                    "date(s.timestamp)", columnAliases, types
            ));
        } else {
            projectionList.add(Projections.sqlGroupProjection(
                    "date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDatetime, count(*) AS statCount",
                    "date(s.timestamp), hour(s.timestamp)", columnAliases, types
            ));
        }
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return criteria.list();
    }

    @Override
    public List<TimeDomainStat> getVisitorTrend(Date since, Date until, Period period) {
        Criteria criteria = getSession().createCriteria(Visitor.class, "v");
        criteria.add(Restrictions.between("v.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        String[] columnAliases = new String[]{"statDatetime", "statCount"};
        Type[] types = new Type[]{new DateType(), new LongType()};
        if (period.getYears() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "makedate(year(v.timestamp), 1) AS statDatetime, count(*) AS statCount",
                    "year(v.timestamp)", columnAliases, types
            ));
        } else if (period.getMonths() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDatetime, count(*) AS statCount",
                    "year(v.timestamp), month(v.timestamp)", columnAliases, types
            ));
        } else if (period.getDays() > 0) {
            projectionList.add(Projections.sqlGroupProjection(
                    "SELECT date(v.timestamp) AS statDatetime, count(*) AS statCount",
                    "date(v.timestamp)", columnAliases, types
            ));
        } else {
            projectionList.add(Projections.sqlGroupProjection(
                    "date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDatetime, count(*) AS statCount",
                    "date(v.timestamp), hour(v.timestamp)", columnAliases, types
            ));
        }
        criteria.setProjection(projectionList);
//        criteria.addOrder(Order.asc("statDatetime"));
        criteria.setResultTransformer(new AliasToBeanResultTransformer(TimeDomainStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getPageviewsByCategory(Date since, Date until) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.post", "p");
        criteria.createAlias("s.category", "c");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "c.name AS statName, count(*) AS statCount",
                "c.name", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getPageviewByAuthor(Date since, Date until) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.post", "p");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "p.displayName AS statName, count(*) AS statCount",
                "p.displayName", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopPages(Date since, Date until, Long limit, Types.ClientType type) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        criteria.createAlias("s.post", "p", JoinType.LEFT_OUTER_JOIN);
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.eq("v.appName", type));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "ifnull(p.title, s.path) AS statName, count(*) AS statCount",
                "s.path", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopPosts(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        criteria.createAlias("s.post", "p");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "p.title AS statName, count(*) AS statCount",
                "p.id", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopPosts(Date since, Date until, Long limit, Types.ClientType type) {
        return null;
    }

    @Override
    public List<ShareStat> getTopLandingPages(Date since, Date until, Long limit, Types.ClientType type) {
        return null;
    }

    @Override
    public List<ShareStat> getTopLandingPosts(Date since, Date until, Long limit, Types.ClientType type) {
        return null;
    }

    @Override
    public List<ShareStat> getSecondaryViewedPages(Date since, Date until, Long limit, Types.ClientType type) {
        return null;
    }

    @Override
    public List<ShareStat> getSecondaryViewedPosts(Date since, Date until, Long limit, Types.ClientType type) {
        return null;
    }

    @Override
    public List<ShareStat> getTopReferrers(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "s.referrer AS statName, count(*) AS statCount",
                "s.referrer", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopWebBrowsers(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Visitor.class, "v");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("v.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "v.browser AS statName, count(*) AS statCount",
                "v.browser", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopOs(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Visitor.class, "v");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("v.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "v.os AS statName, count(*) AS statCount",
                "v.os", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopTags(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        criteria.createAlias("s.tags", "t");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "t.name AS statName, count(*) AS statCount",
                "t.id", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    @Override
    public List<ShareStat> getTopChannel(Date since, Date until, Long limit) {
        Criteria criteria = getSession().createCriteria(Stat.class, "s");
        criteria.createAlias("s.visitor", "v");
        String[] columnAliases = new String[]{"statName", "statCount"};
        Type[] types = new Type[]{new StringType(), new LongType()};

        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.browser", "Googlebot"));
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.sqlGroupProjection(
                "s.channel AS statName, count(*) AS statCount",
                "s.channel", columnAliases, types
        ));

        criteria.setProjection(projectionList);
        criteria.addOrder(Order.desc("statCount"));
        criteria.setMaxResults(limit.intValue());
        criteria.setResultTransformer(new AliasToBeanResultTransformer(ShareStat.class));
        return criteria.list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private void setDateCriteria(Criteria criteria, Date since, Date until) {
        criteria.add(Restrictions.between("timestamp", since, until));
    }
}
