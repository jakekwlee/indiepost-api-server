package com.indiepost.repository;

import com.indiepost.model.analytics.Click;
import org.springframework.data.repository.query.Param;

/**
 * Created by jake on 8/9/17.
 */
public interface ClickRepository {
    void save(Click click);

    Long countByLinkId(Long linkId);

    Long countValidClicksByLinkId(@Param("linkId") Long linkId);

    Long countAllClicksByCampaignId(Long campaignId);

    Long countValidClicksByCampaignId(Long campaignId);
}
