package com.indiepost.service;

import com.indiepost.dto.analytics.*;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.model.Metadata;
import com.indiepost.repository.MetadataRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private final MetadataRepository metadataRepository;

    private final StatRepository statRepository;

    private final VisitorRepository visitorRepository;

    @Inject
    public AnalyticsServiceImpl(MetadataRepository metadataRepository,
                                StatRepository statRepository,
                                VisitorRepository visitorRepository) {
        this.metadataRepository = metadataRepository;
        this.statRepository = statRepository;
        this.visitorRepository = visitorRepository;
    }

    @Override
    public OverviewStats getOverviewStats(PeriodDto periodDto) {
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

        OverviewStats stats = new OverviewStats();
        stats.setPageviewTrend(pageviewTrend);
        stats.setVisitorTrend(visitorTrend);
        stats.setPeriod(periodDto);

        stats.setTotalPageview(statRepository.getTotalPageviews(since, until));
        stats.setTotalUniquePageview(statRepository.getTotalUniquePageviews(since, until));
        stats.setTotalPostview(statRepository.getTotalPostviews(since, until));
        stats.setTotalUniquePostview(statRepository.getTotalUniquePostviews(since, until));

        stats.setTotalVisitor(visitorRepository.getTotalVisitors(since, until));
        stats.setTotalAppVisitor(visitorRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP.toString()));

        return stats;
    }

    @Override
    public TopStats getTopStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);

        TopStats stats = new TopStats();
        stats.setPeriod(periodDto);
        stats.setTopPagesWebapp(statRepository.getTopPages(since, until, 10));
        stats.setTopPosts(statRepository.getTopPosts(since, until, 10));
        stats.setPageviewByAuthor(statRepository.getPageviewsByAuthor(since, until, 10));
        stats.setPageviewByCategory(statRepository.getPageviewsByCategory(since, until, 10));
        stats.setTopBrowser(visitorRepository.getTopWebBrowsers(since, until, 10));
        stats.setTopChannel(visitorRepository.getTopChannel(since, until, 10));
        stats.setTopReferrer(visitorRepository.getTopReferrers(since, until, 10));
        stats.setTopOs(visitorRepository.getTopOs(since, until, 10));
        return stats;
    }


    @Override
    public RecentAndOldPostStats getRecentAndOldPostStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);
        Types.TimeDomainDuration duration = periodDto.getDuration();

        List<TimeDomainDoubleStat> resultStats = statRepository.getRecentAndOldPageviewTrend(since, until, duration);
        DoubleTrend trend = new DoubleTrend();
        trend.setDuration(duration);
        trend.setResult(resultStats);
        trend.setStatName("Time domain old and new post pageviews trend");

        RecentAndOldPostStats stats = new RecentAndOldPostStats();
        stats.setTrend(trend);
        stats.setPeriod(periodDto);
        stats.setTopRecentPosts(statRepository.getTopRecentPosts(since, until, 10));
        stats.setTopOldPosts(statRepository.getTopOldPosts(since, until, 10));
        return stats;
    }

    @Override
    public PostStatsDto getCachedPostStats() {
        List<PostStatDto> statData = statRepository.getCachedPostStats();
        Optional<Metadata> optional = metadataRepository.findById(1L);
        if (optional.isPresent()) {
            Metadata metadata = optional.get();
            LocalDateTime lastUpdated = metadata.getPostStatsLastUpdated();
            return new PostStatsDto(lastUpdated, statData);
        } else {
            return null;
        }

    }

    @Override
    public PostStatsDto getPostStats(PeriodDto periodDto) {
        LocalDate startDate = periodDto.getStartDate();
        LocalDate endDate = periodDto.getEndDate();
        LocalDateTime since = startDate.atStartOfDay();
        LocalDateTime until = endDate.atTime(23, 59, 59);
        List<PostStatDto> postStatDtoList = statRepository.getPostStatsOrderByPageviews(since, until, 3000);
        return new PostStatsDto(LocalDateTime.now(), postStatDtoList, periodDto);
    }
}
