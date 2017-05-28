package com.indiepost.service;

import com.indiepost.dto.stat.*;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    @Autowired
    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @Override
    public SiteStats getStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);

        List<TimeDomainStat> pageviewStatResult;
        List<TimeDomainStat> visitorTrendResult;

        Types.TimeDomainDuration timeDomainDuration = periodDto.getDuration();
        switch (timeDomainDuration) {
            case HOURLY:
                pageviewStatResult = analyticsRepository.getHourlyPageviewTrend(since, until);
                visitorTrendResult = analyticsRepository.getHourlyVisitorTrend(since, until);
                break;
            case DAILY:
                pageviewStatResult = analyticsRepository.getDailyPageviewTrend(since, until);
                visitorTrendResult = analyticsRepository.getDailyVisitorTrend(since, until);
                break;
            case MONTHLY:
                pageviewStatResult = analyticsRepository.getMonthlyPageviewTrend(since, until);
                visitorTrendResult = analyticsRepository.getMonthlyVisitorTrend(since, until);
                break;
            case YEARLY:
                pageviewStatResult = analyticsRepository.getYearlyPageviewTrend(since, until);
                visitorTrendResult = analyticsRepository.getYearlyVisitorTrend(since, until);
                break;
            default:
                pageviewStatResult = new ArrayList<>();
                visitorTrendResult = new ArrayList<>();
                break;
        }

        Trend pageviewTrend = new Trend();
        Trend visitorTrend = new Trend();
        pageviewTrend.setDuration(timeDomainDuration);
        pageviewTrend.setResult(pageviewStatResult);
        visitorTrend.setDuration(timeDomainDuration);
        visitorTrend.setResult(visitorTrendResult);

        SiteStats stats = new SiteStats();
        stats.setPageviewTrend(pageviewTrend);
        stats.setVisitorTrend(visitorTrend);

        stats.setTotalPageview(analyticsRepository.getTotalPageviews(since, until));
        stats.setTotalUniquePageview(analyticsRepository.getTotalUniquePageviews(since, until));
        stats.setTotalUniquePostview(analyticsRepository.getTotalUniquePostviews(since, until));
        stats.setTotalPostview(analyticsRepository.getTotalPostviews(since, until));
        stats.setTotalVisitor(analyticsRepository.getTotalVisitors(since, until));
        stats.setTotalAppVisitor(analyticsRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
        stats.setTopPagesWebapp(analyticsRepository.getTopPages(since, until, 10L));
        stats.setTopPosts(analyticsRepository.getTopPosts(since, until, 10L));
        stats.setPageviewByAuthor(analyticsRepository.getPageviewByAuthor(since, until, 100L));
        stats.setPageviewByCategory(analyticsRepository.getPageviewsByCategory(since, until, 30L));
        stats.setTopBrowser(analyticsRepository.getTopWebBrowsers(since, until, 10L));
        stats.setTopChannel(analyticsRepository.getTopChannel(since, until, 10L));
        stats.setTopReferrer(analyticsRepository.getTopReferrers(since, until, 10L));
        stats.setTopOs(analyticsRepository.getTopOs(since, until, 10L));
        stats.setTopTags(analyticsRepository.getTopTags(since, until, 10L));
        stats.setPostsByPageview(getPostsOrderByPageviews(periodDto));

        return stats;
    }

    @Override
    public List<PostStat> getPostsOrderByPageviews(PeriodDto dto) {
        LocalDateTime since = dto.getStartDate().atStartOfDay();
        LocalDateTime until = dto.getEndDate().atTime(LocalTime.MAX);

        List<PostStat> pageviewList = analyticsRepository.getPostsOrderByPageviews(since, until, 3000L);
        List<PostStat> uniquePageviewList = analyticsRepository.getPostsOrderByUniquePageviews(since, until, 3000L);

        for (PostStat pageview : pageviewList) {
            Long postId = pageview.getId();
            for (PostStat uniquePageview : uniquePageviewList) {
                if (uniquePageview.getId().equals(postId)) {
                    pageview.setUniquePageview(uniquePageview.getUniquePageview());
                    break;
                }
            }
        }
        return pageviewList;
    }
}