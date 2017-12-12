package com.indiepost.repository;

import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainDoubleStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.model.QCachedPostStat;
import com.indiepost.model.QCategory;
import com.indiepost.model.QPost;
import com.indiepost.model.analytics.Stat;
import com.indiepost.repository.utils.PostStatsResultTransformer;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.repository.utils.CriteriaUtils.*;
import static com.indiepost.utils.DateUtil.*;

/**
 * Created by jake on 8/9/17.
 */
@SuppressWarnings("unchecked")
@Repository
public class StatRepositoryHibernate implements StatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Stat stat) {
        return (Long) getSession().save(stat);
    }

    @Override
    public void update(Stat stat) {
        getSession().update(stat);
    }

    @Override
    public void delete(Stat stat) {
        getSession().delete(stat);
    }

    @Override
    public Stat findOne(Long id) {
        return entityManager.find(Stat.class, id);
    }

    @Override
    public Long getTotalPageviews(LocalDateTime since, LocalDateTime until) {
        Criteria criteria = createCriteria();
        criteria.createAlias("visitor", "v");
        criteria.add(Restrictions.ne("s.class", "Click"));
        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.appName", ClientType.INDIEPOST_AD_ENGINE.toString()));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalPageviews(LocalDateTime since, LocalDateTime until, String client) {
        Criteria criteria = createCriteria();
        criteria.createAlias("visitor", "v");
        criteria.add(Restrictions.ne("s.class", "Click"));
        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.eq("v.appName", client));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalPostviews(LocalDateTime since, LocalDateTime until) {
        Criteria criteria = createCriteria();
        criteria.createAlias("visitor", "v");
        criteria.add(Restrictions.isNotNull("s.postId"));
        criteria.add(Restrictions.ne("s.class", "Click"));
        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.ne("v.appName", ClientType.INDIEPOST_AD_ENGINE.toString()));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalPostviews(LocalDateTime since, LocalDateTime until, String client) {
        Criteria criteria = createCriteria();
        criteria.createAlias("visitor", "v");
        criteria.add(Restrictions.isNotNull("s.postId"));
        criteria.add(Restrictions.ne("s.class", "Click"));
        criteria.add(Restrictions.between("s.timestamp", since, until));
        criteria.add(Restrictions.eq("v.appName", client));
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @Override
    public Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS");
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until, String client) {
        Query query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_BY_CLIENT");
        query.setParameter("client", client);
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS");
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until, String client) {
        Query query = getNamedQuery("@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS_BY_CLIENT");
        query.setParameter("client", client);
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    @Override
    public List<TimeDomainStat> getPageviewTrend(LocalDateTime since, LocalDateTime until, TimeDomainDuration duration) {
        switch (duration) {
            case HOURLY:
                return getPageviewTrendHourly(since, until);
            case DAILY:
                return getPageviewTrendDaily(since, until);
            case MONTHLY:
                return getPageviewTrendMonthly(since, until);
            case YEARLY:
                return getPageviewTrendYearly(since, until);
            default:
                return getPageviewTrendHourly(since, until);
        }
    }

    @Override
    public List<TimeDomainDoubleStat> getRecentAndOldPageviewTrend(LocalDateTime since, LocalDateTime until, TimeDomainDuration duration) {
        switch (duration) {
            case HOURLY:
                return getOldAndNewPageviewTrendHourly(since, until);
            case DAILY:
                return getOldAndNewPageviewTrendDaily(since, until);
            case MONTHLY:
                return getOldAndNewPageviewTrendMonthly(since, until);
            case YEARLY:
                return getOldAndNewPageviewTrendYearly(since, until);
            default:
                return getOldAndNewPageviewTrendHourly(since, until);
        }
    }

    @Override
    public List<PostStatDto> getPostStatsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_POST_STATS_ORDER_BY_PAGEVIEWS");
        return getPostShare(query, since, until, limit);
    }

    @Override
    public List<PostStatDto> getAllPostStats() {
        Query query = getNamedQuery("@GET_ALL_POST_STATS");
        query.setResultTransformer(new PostStatsResultTransformer());
        return query.list();
    }

    @Override
    public List<PostStatDto> getAllPostStatsFromCache() {
        QCachedPostStat stat = QCachedPostStat.cachedPostStat;
        List<Tuple> rows = getQueryFactory()
                .from(stat)
                .select(stat.post.id, stat.post.title, stat.post.bylineName,
                        stat.post.category.name, stat.post.publishedAt,
                        stat.pageviews, stat.uniquePageviews,
                        stat.legacyPageviews, stat.legacyUniquePageviews)
                .innerJoin(stat.post, QPost.post)
                .innerJoin(stat.post.category, QCategory.category)
                .where(stat.post.status.eq(Types.PostStatus.PUBLISH))
                .distinct()
                .orderBy(stat.post.publishedAt.desc())
                .fetch();

        return rows.stream().map(tuple -> {
            PostStatDto dto = new PostStatDto();
            dto.setId(tuple.get(stat.post.id));
            dto.setTitle(tuple.get(stat.post.title));
            dto.setAuthor(tuple.get(stat.post.bylineName));
            dto.setCategory(tuple.get(stat.post.category.name));
            dto.setPublishedAt(tuple.get(stat.post.publishedAt));
            dto.setPageviews(tuple.get(stat.pageviews));
            dto.setUniquePageviews(tuple.get(stat.uniquePageviews));
            dto.setLegacyPageviews(tuple.get(stat.legacyPageviews));
            dto.setLegacyUniquePageviews(tuple.get(stat.legacyUniquePageviews));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_PAGEVIEWS_ORDER_BY_CATEGORY");
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_PAGEVIEWS_ORDER_BY_AUTHOR");
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_PAGES");
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_PAGES_BY_CLINT_TYPE");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_POSTS");
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_POSTS_BY_CLINT_TYPE");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_LANDING_PAGE");
        return getShare(query, since, until, limit);
    }

    @Override
    public List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_LANDING_PAGE_BY_CLINT_TYPE");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_TAGS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit, String client) {
        Query query = getNamedQuery("@GET_TOP_TAGS_BY_CLIENT");
        return getShare(query, since, until, limit, client);
    }

    @Override
    public List<ShareStat> getTopRecentPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_RECENT_POSTS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public List<ShareStat> getTopOldPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        Query query = getNamedQuery("@GET_TOP_OLD_POSTS");
        return getShare(query, since, until, limit, null);
    }

    @Override
    public void updatePostStatsCache() {
        Query query = getNamedQuery("@UPDATE_ALL_POST_STATS_CACHE");
        query.executeUpdate();
    }

    @Override
    public void deleteAllPostStatsCache() {
        QCachedPostStat stat = QCachedPostStat.cachedPostStat;
        getQueryFactory()
                .delete(stat)
                .where(stat.id.goe(0L))
                .execute();
    }

    private List<TimeDomainStat> getPageviewTrendHourly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_PAGEVIEW_TREND_HOURLY");
        List<TimeDomainStat> trend = getTrend(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainStats(trend, since.toLocalDate(), until.toLocalDate());
    }

    private List<TimeDomainStat> getPageviewTrendDaily(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_PAGEVIEW_TREND_DAILY");
        return getTrend(query, TimeDomainDuration.DAILY, since, until);
    }

    private List<TimeDomainStat> getPageviewTrendMonthly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_PAGEVIEW_TREND_MONTHLY");
        return getTrend(query, TimeDomainDuration.MONTHLY, since, until);
    }

    private List<TimeDomainStat> getPageviewTrendYearly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_PAGEVIEW_TREND_YEARLY");
        return getTrend(query, TimeDomainDuration.YEARLY, since, until);
    }

    private List<TimeDomainDoubleStat> getOldAndNewPageviewTrendHourly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_HOURLY");
        List<TimeDomainDoubleStat> doubleStats = getDoubleTrend(query, TimeDomainDuration.HOURLY, since, until);
        return normalizeTimeDomainDoubleStats(doubleStats, since.toLocalDate(), until.toLocalDate());
    }

    private List<TimeDomainDoubleStat> getOldAndNewPageviewTrendDaily(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_DAILY");
        return getDoubleTrend(query, TimeDomainDuration.DAILY, since, until);
    }

    private List<TimeDomainDoubleStat> getOldAndNewPageviewTrendMonthly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_MONTHLY");
        return getDoubleTrend(query, TimeDomainDuration.MONTHLY, since, until);
    }

    private List<TimeDomainDoubleStat> getOldAndNewPageviewTrendYearly(LocalDateTime since, LocalDateTime until) {
        Query query = getNamedQuery("@GET_OLD_AND_NEW_PAGEVIEW_TREND_YEARLY");
        return getDoubleTrend(query, TimeDomainDuration.YEARLY, since, until);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Query getNamedQuery(String queryName) {
        return getSession().getNamedQuery(queryName);
    }

    private Criteria createCriteria() {
        return getSession().createCriteria(Stat.class, "s");
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
