package com.indiepost.repository;

import com.indiepost.dto.stat.PostStat;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
public interface StatRepository {

    Long save(Stat stat);

    void delete(Stat stat);

    Stat findById(Long id);

    void update(Stat stat);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime LocalDateTime);

    Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalPostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime until, StatType type);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until, ClientType appName);

    List<PostStat> getPostsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostStat> getPostsOrderByUniquePageviews(LocalDateTime since, LocalDateTime until, Long limit);

    List<TimeDomainStat> getPageviewTrend(LocalDateTime since, LocalDateTime until, Period period);

    List<TimeDomainStat> getVisitorTrend(LocalDateTime since, LocalDateTime until, Period period);

    List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until);

    List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until);

    List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getTopLandingPosts(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getSecondaryViewedPages(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getSecondaryViewedPosts(LocalDateTime since, LocalDateTime until, Long limit, Types.ClientType type);

    List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit);
}
