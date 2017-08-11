package com.indiepost.repository;

import com.indiepost.model.analytics.Click;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by jake on 8/9/17.
 */
public interface ClickRepository extends CrudRepository<Click, Long> {
    Long countByLinkId(Long linkId);

    @Query(nativeQuery = true,
            value = "SELECT count(*) FROM Campaigns c " +
                    "INNER JOIN Links l ON c.id = l.campaignId " +
                    "INNER JOIN Stats s ON l.id = s.linkId " +
                    "WHERE l.id = :linkId " +
                    "AND s.timestamp BETWEEN c.startAt AND c.endAt")
    Long countValidClicksByLinkId(@Param("linkId") Long linkId);
}
