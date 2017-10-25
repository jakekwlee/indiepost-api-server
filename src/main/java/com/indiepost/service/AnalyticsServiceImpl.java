package com.indiepost.service;

import com.indiepost.dto.stat.*;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.model.StatMetadata;
import com.indiepost.repository.StatMetadataRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StatRepository statRepository;

    private final VisitorRepository visitorRepository;

    private final StatMetadataRepository statMetadataRepository;

    @Autowired
    public AnalyticsServiceImpl(StatRepository statRepository, VisitorRepository visitorRepository, StatMetadataRepository statMetadataRepository) {
        this.statRepository = statRepository;
        this.visitorRepository = visitorRepository;
        this.statMetadataRepository = statMetadataRepository;
    }

    @Override
    public SiteStats getStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);

        List<TimeDomainStat> pageviewStatResult;
        List<TimeDomainStat> visitorTrendResult;

        Types.TimeDomainDuration duration = periodDto.getDuration();
        pageviewStatResult = statRepository.getPageviewTrend(since, until, duration);
        visitorTrendResult = visitorRepository.getVisitorTrend(since, until, duration);

        Trend pageviewTrend = new Trend();
        Trend visitorTrend = new Trend();
        pageviewTrend.setDuration(duration);
        pageviewTrend.setResult(pageviewStatResult);
        visitorTrend.setDuration(duration);
        visitorTrend.setResult(visitorTrendResult);

        SiteStats stats = new SiteStats();
        stats.setPageviewTrend(pageviewTrend);
        stats.setVisitorTrend(visitorTrend);

        stats.setTotalPageview(statRepository.getTotalPageviews(since, until));
        stats.setTotalUniquePageview(statRepository.getTotalUniquePageviews(since, until));
        stats.setTotalPostview(statRepository.getTotalPostviews(since, until));
        stats.setTotalUniquePostview(statRepository.getTotalUniquePostviews(since, until));
        stats.setTopPagesWebapp(statRepository.getTopPages(since, until, 10L));
        stats.setTopPosts(statRepository.getTopPosts(since, until, 10L));
        stats.setPageviewByAuthor(statRepository.getPageviewByAuthor(since, until, 100L));
        stats.setPageviewByCategory(statRepository.getPageviewsByCategory(since, until, 30L));
        stats.setTopTags(statRepository.getTopTags(since, until, 10L));

        stats.setTotalVisitor(visitorRepository.getTotalVisitors(since, until));
        stats.setTotalAppVisitor(visitorRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString()));
        stats.setTopBrowser(visitorRepository.getTopWebBrowsers(since, until, 10L));
        stats.setTopChannel(visitorRepository.getTopChannel(since, until, 10L));
        stats.setTopReferrer(visitorRepository.getTopReferrers(since, until, 10L));
        stats.setTopOs(visitorRepository.getTopOs(since, until, 10L));

        return stats;
    }

    @Override
    public List<PostStatDto> getAllPostStats() {
        accumulatePostStats();
        return statRepository.getAllPostStats();
    }

    @Override
    public List<PostStatDto> getPostStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);
        return statRepository.getPostStatsOrderByPageviews(since, until, 3000L);
    }

    @Override
    public void accumulatePostStats() {
        LocalDateTime now = LocalDateTime.now();
        statRepository.updatePostStats(now);
        StatMetadata statMetadata = statMetadataRepository.findOne(1);
        statMetadata.setPostPageviewLastUpdatedAt(now);
        statMetadataRepository.save(statMetadata);
    }
}
