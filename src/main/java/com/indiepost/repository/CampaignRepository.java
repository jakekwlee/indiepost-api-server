package com.indiepost.repository;

import com.indiepost.model.analytics.Campaign;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jake on 8/10/17.
 */
public interface CampaignRepository extends CrudRepository<Campaign, Long> {
    @Query(nativeQuery = true,
            value = "SELECT count(DISTINCT v.id) FROM Campaigns c " +
                    "INNER JOIN Links l ON c.id = l.campaignId " +
                    "INNER JOIN Stats s ON l.id = s.linkId " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.class = 'Click' AND l.campaignId = :campaignId")
    Long countAllClicksByCampaignId(@Param("campaignId") Long campaignId);

    @Query(nativeQuery = true,
            value = "SELECT count(DISTINCT v.id) FROM Campaigns c " +
                    "INNER JOIN Links l ON c.id = l.campaignId " +
                    "INNER JOIN Stats s ON l.id = s.linkId " +
                    "INNER JOIN Visitors v ON s.visitorId = v.id " +
                    "WHERE s.class = 'Click' AND l.campaignId = :campaignId " +
                    "AND s.timestamp BETWEEN c.startAt AND c.endAt")
    Long countValidClicksByCampaignId(@Param("campaignId") Long campaignId);
}
