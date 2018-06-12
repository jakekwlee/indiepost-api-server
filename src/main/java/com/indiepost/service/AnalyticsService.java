package com.indiepost.service;

import com.indiepost.dto.analytics.*;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    OverviewStats getOverviewStats(PeriodDto periodDto);

    RecentAndOldPostStats getRecentAndOldPostStats(PeriodDto periodDto);

    PostStatsDto getPostStats(PeriodDto periodDto);

    PostStatsDto getCachedPostStats();

    TopStats getTopStats(PeriodDto periodDto);
}
