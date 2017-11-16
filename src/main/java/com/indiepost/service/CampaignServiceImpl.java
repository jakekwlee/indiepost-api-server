package com.indiepost.service;

import com.indiepost.dto.analytics.CampaignDto;
import com.indiepost.model.analytics.Campaign;
import com.indiepost.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    private final LinkService linkService;

    @Autowired
    public CampaignServiceImpl(CampaignRepository campaignRepository, LinkService linkService) {
        this.campaignRepository = campaignRepository;
        this.linkService = linkService;
    }

    @Override
    public CampaignDto save(CampaignDto campaignDto) {
        Campaign campaign = dtoToCampaign(campaignDto);
        campaignRepository.save(campaign);
        return campaignToDto(campaign);
    }

    @Override
    public void update(CampaignDto campaignDto) {
        Campaign campaign = dtoToCampaign(campaignDto);
        campaignRepository.save(campaign);
    }

    @Override
    public void deleteById(Long id) {
        campaignRepository.delete(id);
    }

    @Override
    public CampaignDto findById(Long id) {
        Campaign campaign = campaignRepository.findOne(id);
        return campaignToDto(campaign);
    }

    @Override
    public List<CampaignDto> findAll() {
        List<Campaign> campaigns = campaignRepository.findAllByOrderByCreatedAtDesc();
        return campaigns.stream()
                .map(campaign -> campaignToDto(campaign))
                .collect(Collectors.toList());
    }

    @Override
    public Campaign dtoToCampaign(CampaignDto campaignDto) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignDto.getName());
        campaign.setClientName(campaignDto.getClientName());
        campaign.setGoal(campaignDto.getGoal());
        campaign.setStartAt(campaignDto.getStartAt());
        campaign.setEndAt(campaignDto.getEndAt());
        campaign.setCreatedAt(LocalDateTime.now());
        return campaign;
    }

    @Override
    public CampaignDto campaignToDto(Campaign campaign) {
        CampaignDto dto = new CampaignDto();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setClientName(campaign.getClientName());
        dto.setCreateAt(campaign.getCreatedAt());
        dto.setStartAt(campaign.getStartAt());
        dto.setEndAt(campaign.getEndAt());
        dto.setGoal(campaign.getGoal());

        Long allClicks = campaignRepository.countAllClicksByCampaignId(campaign.getId());
        Long validClick = campaignRepository.countValidClicksByCampaignId(campaign.getId());
        dto.setAllClicks(allClicks);
        dto.setValidClicks(validClick);
        return dto;
    }
}
