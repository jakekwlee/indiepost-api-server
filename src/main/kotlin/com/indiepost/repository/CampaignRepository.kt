package com.indiepost.repository

import com.indiepost.dto.analytics.LinkDto
import com.indiepost.dto.analytics.RawDataReportRow
import com.indiepost.dto.analytics.ShareStat
import com.indiepost.dto.analytics.TimeDomainStat
import com.indiepost.model.Banner
import com.indiepost.model.analytics.Campaign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

/**
 * Created by jake on 8/10/17.
 */
interface CampaignRepository {

    fun save(campaign: Campaign)

    fun findOne(id: Long): Campaign?

    fun deleteById(id: Long)

    fun count(): Long

    fun find(pageable: Pageable): Page<Campaign>

    fun findCampaignLinksOrderByClicks(campaignId: Long): List<LinkDto>

    fun getUniqueVisitorGroupByOs(campaignId: Long): List<ShareStat>

    fun getUniqueVisitorGroupByBrowser(campaignId: Long): List<ShareStat>

    fun getTopCampaigns(start: LocalDateTime, end: LocalDateTime, clientName: String, limit: Int): List<ShareStat>

    fun getUniqueVisitorTrendHourly(campaignId: Long): List<TimeDomainStat>

    fun findBannerOnCampaignPeriodByPriority(): List<Banner>

    fun getRawDataReport(campaignId: Long): List<RawDataReportRow>
}
