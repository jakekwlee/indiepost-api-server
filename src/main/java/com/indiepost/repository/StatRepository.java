package com.indiepost.repository;

import com.indiepost.dto.stat.ShareStatResult;
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

    Long getTotalPageviews(Date since, Date until, StatType type);

    Long getTotalVisitors(Date since, Date until);

    Long getTotalVisitors(Date since, Date until, ClientType appName);

    List<TimeDomainStat> getPageviewTrend(Date since, Date until, Period period);

    List<TimeDomainStat> getVisitorTrend(Date since, Date until, Period period);

    List<ShareStatResult> getPageviewsByCategory(Date since, Date until);

    List<ShareStatResult> getPageviewByAuthor(Date since, Date until);

    List<ShareStatResult> getTopPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getTopPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getTopLandingPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getTopLandingPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getSecondaryViewedPages(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getSecondaryViewedPosts(Date since, Date until, Long limit, Types.ClientType type);

    List<ShareStatResult> getTopReferrers(Date since, Date until, Long limit);

    List<ShareStatResult> getTopWebBrowsers(Date since, Date until, Long limit);

    List<ShareStatResult> getTopOs(Date since, Date until, Long limit);

    List<ShareStatResult> getTopTags(Date since, Date until, Long limit);

    List<ShareStatResult> getTopChannel(Date since, Date until, Long limit);
}
