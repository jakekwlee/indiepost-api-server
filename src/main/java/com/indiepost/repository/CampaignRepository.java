package com.indiepost.repository;

import com.indiepost.dto.stat.LinkDto;
import com.indiepost.dto.stat.RawDataReportRow;
import com.indiepost.dto.stat.ShareStat;
import com.indiepost.dto.stat.TimeDomainStat;
import com.indiepost.model.Banner;
import com.indiepost.model.analytics.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by jake on 8/10/17.
 */
public interface CampaignRepository {

    void save(Campaign campaign);

    Optional<Campaign> findOne(Long id);

    void deleteById(Long id);

    Long count();

    Page<Campaign> find(Pageable pageable);

    List<LinkDto> findCampaignLinksOrderByClicks(Long campaignId);

    List<ShareStat> getUniqueVisitorGroupByOs(Long campaignId);

    List<ShareStat> getUniqueVisitorGroupByBrowser(Long campaignId);

    List<ShareStat> getTopCampaigns(LocalDateTime start, LocalDateTime end, String clientName, int limit);

    List<TimeDomainStat> getUniqueVisitorTrendHourly(Long campaignId);

    List<Banner> findBannerOnCampaignPeriodByPriority();

    List<RawDataReportRow> getRawDataReport(Long campaignId);
}
