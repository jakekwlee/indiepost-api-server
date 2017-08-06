package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.analytics.Stat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 5. 24.
 */
public interface AnalyticsRepository {
    Long save(Stat stat);

    void delete(Stat stat);

    Stat findById(Long id);

    void update(Stat stat);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime until);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime until, StatType client);

    Long getTotalPostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until, ClientType client);

    Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until, ClientType client);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until, ClientType client);

    List<TimeDomainStat> getHourlyPageviewTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getDailyPageviewTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getMonthlyPageviewTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getYearlyPageviewTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getHourlyVisitorTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getDailyVisitorTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getMonthlyVisitorTrend(LocalDateTime since, LocalDateTime until);

    List<TimeDomainStat> getYearlyVisitorTrend(LocalDateTime since, LocalDateTime until);

    List<PostStat> getPostsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostStat> getPostsOrderByUniquePageviews(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);

    List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit, ClientType client);
}
