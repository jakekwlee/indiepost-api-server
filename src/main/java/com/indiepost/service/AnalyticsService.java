package com.indiepost.service;

import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.PostStatDto;
import com.indiepost.dto.stat.SiteStats;

import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    SiteStats getStats(PeriodDto periodDto);

    List<PostStatDto> getAllPostStats();

    void accumulatePostStats();
}
