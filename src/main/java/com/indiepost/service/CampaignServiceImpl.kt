package com.indiepost.service

import com.indiepost.dto.analytics.*
import com.indiepost.enums.Types
import com.indiepost.model.Banner
import com.indiepost.model.analytics.Campaign
import com.indiepost.model.analytics.Link
import com.indiepost.repository.CampaignRepository
import com.indiepost.repository.ClickRepository
import com.indiepost.utils.DateUtil.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.inject.Inject
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
class CampaignServiceImpl @Inject constructor(
        private val campaignRepository: CampaignRepository,
        private val clickRepository: ClickRepository) : CampaignService {

    override fun save(campaignDto: CampaignDto): Long? {
        val campaign = Campaign()
        campaign.name = campaignDto.name
        campaign.clientName = campaignDto.clientName
        campaign.goal = campaignDto.goal
        campaign.startAt = instantToLocalDateTime(campaignDto.startAt!!)
        campaign.endAt = instantToLocalDateTime(campaignDto.endAt!!)
        campaign.createdAt = LocalDateTime.now()
        val dtoList = campaignDto.links
        if (dtoList.isNotEmpty()) {
            val newLinks = dtoList.stream()
                    .filter { (id) -> id == null }
                    .map { dto -> linkDtoToNewLink(dto, campaign) }
                    .collect(Collectors.toList())
            campaign.links = newLinks
        }
        campaignRepository.save(campaign)
        return campaign.id
    }

    override fun update(campaignDto: CampaignDto) {
        val campaign = campaignRepository.findOne(campaignDto.id!!)
                ?: throw EntityNotFoundException("No campaign with this id: " + campaignDto.id!!)
        campaign.name = campaignDto.name
        campaign.clientName = campaignDto.clientName
        campaign.goal = campaignDto.goal
        campaign.startAt = instantToLocalDateTime(campaignDto.startAt!!)
        campaign.endAt = instantToLocalDateTime(campaignDto.endAt!!)
        campaign.createdAt = LocalDateTime.now()
        val dtoList = campaignDto.links
        if (dtoList.isEmpty()) {
            return
        }
        val updatedLinks = dtoList.stream()
                .filter { (_, _, _, _, _, _, _, _, _, _, isUpdated) -> isUpdated }
                .collect(Collectors.toList())
        for ((id, name, url) in updatedLinks) {
            for (link in campaign.links) {
                if (link.id == id) {
                    link.name = name
                    link.url = url
                }
            }

        }
        val newLinks = dtoList.stream()
                .filter { (id) -> id == null }
                .map { dto -> linkDtoToNewLink(dto, campaign) }
                .collect(Collectors.toList())
        if (newLinks.size > 0) {
            campaign.links.addAll(newLinks)
        }
    }

    override fun deleteById(id: Long?) {
        campaignRepository.deleteById(id!!)
    }

    override fun findById(id: Long?): CampaignDto? {
        val campaign = campaignRepository.findOne(id!!)
        return if (campaign != null) {
            campaignToDto(campaign, true)
        } else {
            null
        }
    }

    override fun find(pageable: Pageable): Page<CampaignDto> {
        val result = campaignRepository.find(pageable)
        val dtoList = result.content.stream()
                .map { campaign -> campaignToDto(campaign) }
                .collect(Collectors.toList())
        return PageImpl(dtoList, result.pageable, result.totalElements)
    }

    override fun dtoToCampaign(campaignDto: CampaignDto): Campaign {
        val campaign = Campaign()
        campaign.name = campaignDto.name
        campaign.clientName = campaignDto.clientName
        campaign.goal = campaignDto.goal
        campaign.startAt = instantToLocalDateTime(campaignDto.startAt!!)
        campaign.endAt = instantToLocalDateTime(campaignDto.endAt!!)
        campaign.createdAt = LocalDateTime.now()
        return campaign
    }

    override fun findBannersOnCampaignPeriod(): List<BannerDto> {
        val bannerList = campaignRepository.findBannerOnCampaignPeriodByPriority()
        val linkUriPath = "https://www.indiepost.co.kr/link/"
        return bannerList.stream().map { banner ->
            val dto = BannerDto()
            dto.id = banner.id
            dto.title = banner.title
            dto.subtitle = banner.subtitle
            dto.bannerType = banner.bannerType!!.toString()
            banner.bgColor?.let {
                dto.bgColor = it
            }
            dto.internalUrl = banner.internalUrl
            dto.imageUrl = banner.imageUrl
            dto.internalUrl = banner.internalUrl
            dto.isCover = banner.isCover
            dto.priority = banner.priority
            dto.target = banner.target.toString()
            val link = banner.link
            if (link != null) {
                dto.linkTo = linkUriPath + link.uid!!
            }
            dto
        }.collect(Collectors.toList())
    }

    override fun campaignToDto(campaign: Campaign): CampaignDto {
        return this.campaignToDto(campaign, false)
    }

    override fun campaignToDto(campaign: Campaign, withLinks: Boolean): CampaignDto {
        val dto = CampaignDto()
        dto.id = campaign.id
        dto.name = campaign.name
        dto.clientName = campaign.clientName
        dto.createdAt = campaign.createdAt
        dto.startAt = localDateTimeToInstant(campaign.startAt!!)
        dto.endAt = localDateTimeToInstant(campaign.endAt!!)
        dto.goal = campaign.goal

        val campaignId = campaign.id
        val validClick = clickRepository.countValidClicksByCampaignId(campaignId!!)
        dto.validClicks = validClick
        if (withLinks) {
            val allClicks = clickRepository.countAllClicksByCampaignId(campaignId)
            dto.allClicks = allClicks
            val links = campaignRepository.findCampaignLinksOrderByClicks(campaignId)
            dto.links = links
        }
        return dto
    }

    override fun getReport(id: Long?): CampaignReport {
        val campaign = campaignRepository.findOne(id!!) ?: return CampaignReport()
        val dto = campaignToDto(campaign, true)
        val byLinks = dto.links
                .stream()
                .map { (_, name, _, _, _, _, _, _, validClicks) -> ShareStat(name!!, validClicks!!) }
                .collect(Collectors.toList())

        val start = instantToLocalDateTime(dto.startAt!!)
        val end = instantToLocalDateTime(dto.endAt!!)
        var trend = campaignRepository.getUniqueVisitorTrendHourly(id)
        trend = normalizeHoursOFTimeDomainStats(trend, start.toLocalDate(), end.toLocalDate())
        val byOs = campaignRepository.getUniqueVisitorGroupByOs(id)
        val byBrowsers = campaignRepository.getUniqueVisitorGroupByBrowser(id)

        val since = start.minusDays(60)
        val topPrevious = campaignRepository.getTopCampaigns(since, end, dto.clientName!!, 10)
        val rawData = campaignRepository.getRawDataReport(id)

        val report = CampaignReport()
        report.byOS = byOs
        report.byLinks = byLinks
        report.byBrowsers = byBrowsers
        report.topPrevious = topPrevious
        report.trend = trend
        report.campaign = dto
        report.rawData = rawData
        return report
    }

    private fun linkDtoToNewLink(dto: LinkDto, campaign: Campaign): Link {
        val link = Link()
        link.campaign = campaign
        link.createdAt = LocalDateTime.now()
        link.name = dto.name
        link.uid = dto.uid
        link.url = dto.url
        if (dto.banner != null) {
            val bannerDto = dto.banner
            val banner = Banner()
            banner.title = bannerDto!!.title
            banner.subtitle = bannerDto.subtitle
            bannerDto.bannerType?.let {
                banner.bannerType = Types.BannerType.valueOf(it)
            }
            banner.bgColor = bannerDto.bgColor
            banner.internalUrl = bannerDto.internalUrl
            banner.imageUrl = bannerDto.imageUrl
            banner.priority = bannerDto.priority
            link.banner = banner
        }
        return link
    }
}
