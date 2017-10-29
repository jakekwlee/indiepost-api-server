package com.indiepost.service;

import com.indiepost.dto.stat.*;

import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    OverviewStats getOverviewStats(PeriodDto periodDto);

    RecentAndOldPostStats getRecentAndOldPostStats(PeriodDto periodDto);

    List<PostStatDto> getPostStats(PeriodDto periodDto);

    PostStatsDto getAllPostStats();

    void updateCachedPostStats();
}
