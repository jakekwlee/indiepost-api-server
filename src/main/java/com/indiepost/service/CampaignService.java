package com.indiepost.service;

import com.indiepost.dto.analytics.CampaignDto;
import com.indiepost.model.analytics.Campaign;

import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface CampaignService {

    CampaignDto save(CampaignDto campaignDto);

    void update(CampaignDto campaignDto);

    void deleteById(Long id);

    CampaignDto findById(Long id);

    List<CampaignDto> findAll();

    Campaign dtoToCampaign(CampaignDto campaignDto);

    CampaignDto campaignToDto(Campaign campaign);
}
