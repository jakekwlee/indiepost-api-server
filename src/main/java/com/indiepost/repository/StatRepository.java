package com.indiepost.repository;

import com.indiepost.dto.stat.PostStatDto;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainDoubleStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.enums.Types.TimeDomainDuration;
import com.indiepost.model.analytics.Stat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 8/9/17.
 */
public interface StatRepository {
    Long save(Stat stat);

    void update(Stat stat);

    void delete(Stat stat);

    Stat findOne(Long id);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime until);

    Long getTotalPageviews(LocalDateTime since, LocalDateTime until, String client);

    Long getTotalPostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalPostviews(LocalDateTime since, LocalDateTime until, String client);

    Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePageviews(LocalDateTime since, LocalDateTime until, String client);

    Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until);

    Long getTotalUniquePostviews(LocalDateTime since, LocalDateTime until, String client);

    List<TimeDomainStat> getPageviewTrend(LocalDateTime since, LocalDateTime until, TimeDomainDuration duration);

    List<TimeDomainDoubleStat> getRecentAndOldPageviewTrend(LocalDateTime since, LocalDateTime until, TimeDomainDuration duration);

    List<PostStatDto> getPostStatsOrderByPageviews(LocalDateTime since, LocalDateTime until, Long limit);

    List<PostStatDto> getAllPostStats();

    List<PostStatDto> getAllPostStatsFromCache();

    List<ShareStat> getPageviewsByCategory(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getPageviewByAuthor(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPages(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopPosts(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopLandingPages(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopTags(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopRecentPosts(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopOldPosts(LocalDateTime since, LocalDateTime until, Long limit);

    void updatePostStatsCache();

    void deleteAllPostStatsCache();

}
