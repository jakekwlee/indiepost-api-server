package com.indiepost.repository

import com.indiepost.model.analytics.Click
import org.springframework.data.repository.query.Param

/**
 * Created by jake on 8/9/17.
 */
interface ClickRepository {
    fun save(click: Click)

    fun countByLinkId(linkId: Long): Long

    fun countValidClicksByLinkId(@Param("linkId") linkId: Long): Long

    fun countAllClicksByCampaignId(campaignId: Long): Long

    fun countValidClicksByCampaignId(campaignId: Long): Long
}
