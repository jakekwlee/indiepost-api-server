package com.indiepost.service;

import com.indiepost.dto.stat.OverviewStats;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.PostStatsDto;
import com.indiepost.dto.stat.RecentAndOldPostStats;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    OverviewStats getOverviewStats(PeriodDto periodDto);

    RecentAndOldPostStats getRecentAndOldPostStats(PeriodDto periodDto);

    PostStatsDto getPostStats(PeriodDto periodDto);

    PostStatsDto getCachedPostStats();

}
