package com.indiepost.repository;

import com.indiepost.model.analytics.Campaign;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Created by jake on 8/10/17.
 */
public class CampaignRepositoryTest {
    @Autowired
    private CampaignRepository campaignRepository;

    @Test
    public void saveShouldWorkProperly() {
        Campaign campaign = new Campaign();
        campaign.setCreatedAt(LocalDateTime.now());
    }
}
