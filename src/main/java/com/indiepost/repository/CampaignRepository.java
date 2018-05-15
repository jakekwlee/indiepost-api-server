package com.indiepost.repository;

import com.indiepost.model.analytics.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
