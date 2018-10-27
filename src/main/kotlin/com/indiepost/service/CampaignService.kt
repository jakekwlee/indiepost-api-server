package com.indiepost.service

import com.indiepost.dto.analytics.BannerDto
import com.indiepost.dto.analytics.CampaignDto
import com.indiepost.dto.analytics.CampaignReport
import com.indiepost.model.analytics.Campaign
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Created by jake on 8/10/17.
 */
interface CampaignService {
    fun save(campaignDto: CampaignDto): Long?

    fun update(campaignDto: CampaignDto)

    fun deleteById(id: Long)

    fun findById(id: Long): CampaignDto?

    fun find(pageable: Pageable): Page<CampaignDto>

    fun dtoToCampaign(campaignDto: CampaignDto): Campaign

    fun findBannersOnCampaignPeriod(): List<BannerDto>

    fun campaignToDto(campaign: Campaign): CampaignDto

    fun campaignToDto(campaign: Campaign, withLinks: Boolean): CampaignDto

    fun getReport(id: Long): CampaignReport
}
