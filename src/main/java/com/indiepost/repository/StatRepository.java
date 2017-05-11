package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;

import java.time.Period;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
public interface StatRepository {

    Long save(Stat stat);

    void delete(Stat stat);

    Stat findById(Long id);

    void update(Stat stat);

    Long getTotalPageviews(Date since, Date until);

    Long getTotalUniquePageviews(Date since, Date until);

    Long getTotalUniquePostviews(Date since, Date until);

    Long getTotalPostviews(Date since, Date until);

    Long getTotalPageviews(Date since, Date until, StatType type);

    Long getTotalVisitors(Date since, Date until);

    Long getTotalVisitors(Date since, Date until, ClientType appName);

    List<PostStat> getPostsOrderByPageviews(Date since, Date until, Long limit);

    List<PostStat> getPostsOrderByUniquePageviews(Date since, Date until, Long limit);

    List<TimeDomainStat> getPageviewTrend(Date since, Date until, Period period);

    List<TimeDomainStat> getVisitorTrend(Date since, Date until, Period period);

    List<ShareStat> getPageviewsByCategory(Date since, Date until);

    List<ShareStat> getPageviewByAuthor(Date since, Date until);

    List<ShareStat> getTopPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getTopPosts(Date since, Date until, Long limit);

    List<ShareStat> getTopPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getTopLandingPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getTopLandingPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getSecondaryViewedPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getSecondaryViewedPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStat> getTopReferrers(Date since, Date until, Long limit);

    List<ShareStat> getTopWebBrowsers(Date since, Date until, Long limit);

    List<ShareStat> getTopOs(Date since, Date until, Long limit);

    List<ShareStat> getTopTags(Date since, Date until, Long limit);

    List<ShareStat> getTopChannel(Date since, Date until, Long limit);
}
