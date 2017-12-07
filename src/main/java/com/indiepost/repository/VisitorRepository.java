package com.indiepost.repository;

import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.enums.Types;
import com.indiepost.model.analytics.Visitor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 4. 9.
 */
public interface VisitorRepository {

    Long save(Visitor visitor);

    void update(Visitor visitor);

    void delete(Visitor visitor);

    Visitor findOne(Long id);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until);

    Long getTotalVisitors(LocalDateTime since, LocalDateTime until, String client);

    List<TimeDomainStat> getVisitorTrend(LocalDateTime since, LocalDateTime until, Types.TimeDomainDuration duration);

    List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopReferrers(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopWebBrowsers(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopOs(LocalDateTime since, LocalDateTime until, Long limit, String client);

    List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit);

    List<ShareStat> getTopChannel(LocalDateTime since, LocalDateTime until, Long limit, String client);
}