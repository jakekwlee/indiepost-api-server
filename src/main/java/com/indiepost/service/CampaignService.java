package com.indiepost.service;

import com.indiepost.dto.CampaignDto;
import com.indiepost.model.analytics.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by jake on 8/10/17.
 */
public interface CampaignService {
    CampaignDto save(CampaignDto campaignDto);

    void update(CampaignDto campaignDto);

    void deleteById(Long id);

    CampaignDto findById(Long id);

    Page<CampaignDto> find(Pageable pageable);

    Campaign dtoToCampaign(CampaignDto campaignDto);

    CampaignDto campaignToDto(Campaign campaign);
}
