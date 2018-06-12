package com.indiepost.service;

import com.indiepost.dto.analytics.*;
import com.indiepost.enums.Types;
import com.indiepost.model.Banner;
import com.indiepost.model.analytics.Campaign;
import com.indiepost.model.analytics.Link;
import com.indiepost.repository.CampaignRepository;
import com.indiepost.repository.ClickRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.indiepost.utils.DateUtil.*;

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    private final ClickRepository clickRepository;

    @Inject
    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               ClickRepository clickRepository) {
        this.campaignRepository = campaignRepository;
        this.clickRepository = clickRepository;
    }

    @Override
    public Long save(CampaignDto campaignDto) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignDto.getName());
        campaign.setClientName(campaignDto.getClientName());
        campaign.setGoal(campaignDto.getGoal());
        campaign.setStartAt(instantToLocalDateTime(campaignDto.getStartAt()));
        campaign.setEndAt(instantToLocalDateTime(campaignDto.getEndAt()));
        campaign.setCreatedAt(LocalDateTime.now());
        List<LinkDto> dtoList = campaignDto.getLinks();
        if (dtoList != null && dtoList.size() > 0) {
            List<Link> newLinks = dtoList.stream()
                    .filter(dto -> dto.getId() == null)
                    .map(dto -> linkDtoToNewLink(dto, campaign))
                    .collect(Collectors.toList());
            campaign.setLinks(newLinks);
        }
        campaignRepository.save(campaign);
        return campaign.getId();
    }

    @Override
    public void update(CampaignDto campaignDto) {
        Optional<Campaign> optionalCampaign = campaignRepository.findOne(campaignDto.getId());
        if (!optionalCampaign.isPresent()) {
            //TODO
            return;
        }
        Campaign campaign = optionalCampaign.get();
        campaign.setName(campaignDto.getName());
        campaign.setClientName(campaignDto.getClientName());
        campaign.setGoal(campaignDto.getGoal());
        campaign.setStartAt(instantToLocalDateTime(campaignDto.getStartAt()));
        campaign.setEndAt(instantToLocalDateTime(campaignDto.getEndAt()));
        campaign.setCreatedAt(LocalDateTime.now());
        List<LinkDto> dtoList = campaignDto.getLinks();
        if (dtoList == null || dtoList.size() == 0) {
            return;
        }
        List<LinkDto> updatedLinks = dtoList.stream()
                .filter(dto -> dto.isUpdated())
                .collect(Collectors.toList());
        for (LinkDto updatedLink : updatedLinks) {
            for (Link link : campaign.getLinks()) {
                if (link.getId().equals(updatedLink.getId())) {
                    link.setName(updatedLink.getName());
                    link.setUrl(updatedLink.getUrl());
                }
            }

        }
        List<Link> newLinks = dtoList.stream()
                .filter(dto -> dto.getId() == null)
                .map(dto -> linkDtoToNewLink(dto, campaign))
                .collect(Collectors.toList());
        if (newLinks.size() > 0) {
            campaign.getLinks().addAll(newLinks);
        }
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
        campaign.setStartAt(instantToLocalDateTime(campaignDto.getStartAt()));
        campaign.setEndAt(instantToLocalDateTime(campaignDto.getEndAt()));
        campaign.setCreatedAt(LocalDateTime.now());
        return campaign;
    }

    @Override
    public List<BannerDto> findBannersOnCampaignPeriod() {
        List<Banner> bannerList = campaignRepository.findBannerOnCampaignPeriodByPriority();
        if (bannerList == null) {
            return new ArrayList<>();
        }
        String linkUriPath = "https://www.indiepost.co.kr/link/";
        return bannerList.stream().map(banner -> {
            BannerDto dto = new BannerDto();
            dto.setId(banner.getId());
            dto.setTitle(banner.getTitle());
            dto.setSubtitle(banner.getSubtitle());
            dto.setBannerType(banner.getBannerType().toString());
            dto.setBgColor(banner.getBgColor());
            dto.setInternalUrl(banner.getInternalUrl());
            dto.setImageUrl(banner.getImageUrl());
            dto.setInternalUrl(banner.getInternalUrl());
            dto.setCover(banner.isCover());
            dto.setPriority(banner.getPriority());
            Link link = banner.getLink();
            if (link != null) {
                dto.setLinkTo(linkUriPath + link.getUid());
            }
            return dto;
        }).collect(Collectors.toList());
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
        dto.setStartAt(localDateTimeToInstant(campaign.getStartAt()));
        dto.setEndAt(localDateTimeToInstant(campaign.getEndAt()));
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

    @Override
    public CampaignReport getReport(Long id) {
        Optional<Campaign> optional = campaignRepository.findOne(id);
        if (!optional.isPresent()) {
            return new CampaignReport();
        }
        CampaignDto dto = campaignToDto(optional.get(), true);
        List<ShareStat> byLinks = dto.getLinks()
                .stream()
                .map(link -> new ShareStat(link.getName(), link.getValidClicks()))
                .collect(Collectors.toList());
        dto.setLinks(null);

        LocalDateTime start = instantToLocalDateTime(dto.getStartAt());
        LocalDateTime end = instantToLocalDateTime(dto.getEndAt());
        List<TimeDomainStat> trend = campaignRepository.getUniqueVisitorTrendHourly(id);
        trend = normalizeHoursOFTimeDomainStats(trend, start.toLocalDate(), end.toLocalDate());
        List<ShareStat> byOs = campaignRepository.getUniqueVisitorGroupByOs(id);
        List<ShareStat> byBrowsers = campaignRepository.getUniqueVisitorGroupByBrowser(id);

        LocalDateTime since = start.minusDays(60);
        List<ShareStat> topPrevious = campaignRepository.getTopCampaigns(since, end, dto.getClientName(), 10);
        List<RawDataReportRow> rawData = campaignRepository.getRawDataReport(id);

        CampaignReport report = new CampaignReport();
        report.setByOS(byOs);
        report.setByLinks(byLinks);
        report.setByBrowsers(byBrowsers);
        report.setTopPrevious(topPrevious);
        report.setTrend(trend);
        report.setCampaign(dto);
        report.setRawData(rawData);
        return report;
    }

    private Link linkDtoToNewLink(LinkDto dto, Campaign campaign) {
        Link link = new Link();
        link.setCampaign(campaign);
        link.setCreatedAt(LocalDateTime.now());
        link.setName(dto.getName());
        link.setUid(dto.getUid());
        link.setUrl(dto.getUrl());
        if (dto.getBanner() != null) {
            BannerDto bannerDto = dto.getBanner();
            Banner banner = new Banner();
            banner.setTitle(bannerDto.getTitle());
            banner.setSubtitle(bannerDto.getSubtitle());
            banner.setBannerType(Types.BannerType.valueOf(bannerDto.getBannerType()));
            banner.setBgColor(bannerDto.getBgColor());
            banner.setInternalUrl(bannerDto.getInternalUrl());
            banner.setImageUrl(bannerDto.getImageUrl());
            banner.setPriority(bannerDto.getPriority());
            link.setBanner(banner);
        }

        return link;
    }
}
