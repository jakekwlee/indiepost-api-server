package com.indiepost.service;

import com.indiepost.dto.LinkDto;
import com.indiepost.dto.stat.CampaignDto;
import com.indiepost.model.analytics.Campaign;
import com.indiepost.repository.CampaignRepository;
import com.indiepost.repository.ClickRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    private final ClickRepository clickRepository;

    @Autowired
    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               ClickRepository clickRepository) {
        this.campaignRepository = campaignRepository;
        this.clickRepository = clickRepository;
    }

    @Override
    public CampaignDto save(CampaignDto campaignDto) {
        // TODO
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
        campaignRepository.deleteById(id);
    }

    @Override
    public CampaignDto findById(Long id) {
        Optional<Campaign> optional = campaignRepository.findOne(id);
        if (optional.isPresent()) {
            Campaign campaign = optional.get();
            return campaignToDto(campaign, true);
        } else {
            return null;
        }
    }

    @Override
    public Page<CampaignDto> find(Pageable pageable) {
        Page<Campaign> result = campaignRepository.find(pageable);
        List<CampaignDto> dtoList = result.getContent().stream()
                .map(campaign -> campaignToDto(campaign))
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, result.getPageable(), result.getTotalElements());
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
        return this.campaignToDto(campaign, false);
    }

    @Override
    public CampaignDto campaignToDto(Campaign campaign, boolean withLinks) {
        CampaignDto dto = new CampaignDto();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setClientName(campaign.getClientName());
        dto.setCreatedAt(campaign.getCreatedAt());
        dto.setStartAt(campaign.getStartAt());
        dto.setEndAt(campaign.getEndAt());
        dto.setGoal(campaign.getGoal());

        Long campaignId = campaign.getId();
        Long validClick = clickRepository.countValidClicksByCampaignId(campaignId);
        dto.setValidClicks(validClick);
        if (withLinks) {
            Long allClicks = clickRepository.countAllClicksByCampaignId(campaignId);
            dto.setAllClicks(allClicks);
            List<LinkDto> links = campaignRepository.findCampaignLinksOrderByClicks(campaignId);
            dto.setLinks(links);
        }
        return dto;
    }
}
