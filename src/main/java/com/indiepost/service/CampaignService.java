package com.indiepost.service;

import com.indiepost.dto.stat.BannerDto;
import com.indiepost.dto.stat.CampaignDto;
import com.indiepost.dto.stat.CampaignReport;
import com.indiepost.model.analytics.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface CampaignService {
    Long save(CampaignDto campaignDto);

    void update(CampaignDto campaignDto);

    void deleteById(Long id);

    CampaignDto findById(Long id);

    Page<CampaignDto> find(Pageable pageable);

    Campaign dtoToCampaign(CampaignDto campaignDto);

    List<BannerDto> findBannersOnCampaignPeriod();

    CampaignDto campaignToDto(Campaign campaign);

    CampaignDto campaignToDto(Campaign campaign, boolean withLinks);

    CampaignReport getReport(Long id);
}
