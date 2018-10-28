package com.indiepost.repository

import com.indiepost.IndiepostBackendApplication
import com.indiepost.dto.analytics.LinkDto
import com.indiepost.dto.analytics.ShareStat
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import java.util.*
import javax.inject.Inject
import javax.transaction.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(IndiepostBackendApplication::class))
@WebAppConfiguration
@Transactional
class CampaignRepositoryTests {

    @Inject
    private lateinit var repository: CampaignRepository

    @Test
    fun findCampaignLinksOrderByClicks_returnListOfLinkDtoProperly() {
        val dtoList = repository.findCampaignLinksOrderByClicks(1L)
        assertThat(dtoList).hasAtLeastOneElementOfType(LinkDto::class.java)
        for ((id, name, url, campaignId, uid, _, _, _, validClicks, createdAt) in dtoList) {
            assertThat(validClicks).isGreaterThanOrEqualTo(0)
            assertThat(id).isNotNull()
            assertThat(campaignId).isNotNull()
            assertThat(url).isNotNull()
            assertThat(uid).isNotNull()
            assertThat(name).isNotNull()
            assertThat(createdAt).isNotNull()
        }
    }

    @Test
    fun getUniqueVisitorGroupByOs_returnListOfLinkDtoProperly() {
        val expectedStats = ArrayList<ShareStat>()
        expectedStats.add(ShareStat("iOS", 119L))
        expectedStats.add(ShareStat("Android", 107L))
        expectedStats.add(ShareStat("Windows 7", 24L))
        expectedStats.add(ShareStat("Windows 10", 21L))
        expectedStats.add(ShareStat("Mac OS X", 12L))
        expectedStats.add(ShareStat("Linux", 3L))
        expectedStats.add(ShareStat("Other", 3L))
        expectedStats.add(ShareStat("Windows 8.1", 2L))
        expectedStats.add(ShareStat("Windows XP", 2L))
        expectedStats.add(ShareStat("Windows 8", 1L))

        val resultStats = repository.getUniqueVisitorGroupByOs(25L)
        assertThat(resultStats).hasAtLeastOneElementOfType(ShareStat::class.java)
        assertThat(resultStats).hasSize(10)
        for (i in 0..9) {
            val (statName, statValue) = expectedStats[i]
            val (statName1, statValue1) = resultStats[i]
            assertThat(statName).isEqualTo(statName1)
            assertThat(statValue).isEqualTo(statValue1)
        }
    }

    @Test
    fun findBannerOnCampaignPeriodByPriority_shouldWorkProperly() {
        val bannerList = repository.findBannerOnCampaignPeriodByPriority()
        assertThat(bannerList.size).isGreaterThan(3)
    }
}
