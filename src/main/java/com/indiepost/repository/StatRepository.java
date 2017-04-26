package com.indiepost.repository;

import com.indiepost.dto.stat.ShareStatResult;
import com.indiepost.dto.stat.TimeDomainStatResult;
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

    List<TimeDomainStatResult> getPageviewTrend(Date since, Date until, Period period);

    List<TimeDomainStatResult> getVisitorTrend(Date since, Date until, Period period);

    List<ShareStatResult> getPageviewsByCategory(Date since, Date until);

    List<ShareStatResult> getPageviewByAuthor(Date since, Date until);

    List<ShareStatResult> getMostViewedPages(Date since, Date until, Long limit);

    List<ShareStatResult> getMostViewedPosts(Date since, Date until, Long limit);

    List<ShareStatResult> getTopLandingPages(Date since, Date until, Long limit);

    List<ShareStatResult> getTopLandingPosts(Date since, Date until, Long limit);

    List<ShareStatResult> getSecondlyViewedPages(Date since, Date until, Long limit);

    List<ShareStatResult> getSecondlyViewedPosts(Date since, Date until, Long limit);

    List<ShareStatResult> getTopReferrers(Date since, Date until, Long limit);

    List<ShareStatResult> getTopWebBrowsers(Date since, Date until, Long limit);

    List<ShareStatResult> getTopOs(Date since, Date until, Long limit);

    List<ShareStatResult> getTopTags(Date since, Date until, Long limit);

    List<ShareStatResult> getTopChannel(Date since, Date until, Long limit);
}
