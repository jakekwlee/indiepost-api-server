package com.indiepost.service;

import com.indiepost.dto.analytics.*;

import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    Overview getOverviewStats(PeriodDto periodDto);

    RecentAndOldPostStats getRecentAndOldPostStats(PeriodDto periodDto);

    List<PostStatDto> getPostStats(PeriodDto periodDto);

    PostStatsDto getAllPostStats();
}
