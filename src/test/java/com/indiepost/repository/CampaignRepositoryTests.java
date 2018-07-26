package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.analytics.LinkDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.model.Banner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class CampaignRepositoryTests {

    @Inject
    private CampaignRepository repository;

    @Test
    public void findCampaignLinksOrderByClicks_returnListOfLinkDtoProperly() {
        List<LinkDto> dtoList = repository.findCampaignLinksOrderByClicks(1L);
        assertThat(dtoList).hasAtLeastOneElementOfType(LinkDto.class);
        for (LinkDto dto : dtoList) {
            assertThat(dto.getValidClicks()).isGreaterThanOrEqualTo(0);
            assertThat(dto.getId()).isNotNull();
            assertThat(dto.getCampaignId()).isNotNull();
            assertThat(dto.getUrl()).isNotNull();
            assertThat(dto.getUid()).isNotNull();
            assertThat(dto.getName()).isNotNull();
            assertThat(dto.getCreatedAt()).isNotNull();
        }
    }

    @Test
    public void getUniqueVisitorGroupByOs_returnListOfLinkDtoProperly() {
        List<ShareStat> expectedStats = new ArrayList<>();
        expectedStats.add(new ShareStat("iOS", 119L));
        expectedStats.add(new ShareStat("Android", 107L));
        expectedStats.add(new ShareStat("Windows 7", 24L));
        expectedStats.add(new ShareStat("Windows 10", 21L));
        expectedStats.add(new ShareStat("Mac OS X", 12L));
        expectedStats.add(new ShareStat("Linux", 3L));
        expectedStats.add(new ShareStat("Other", 3L));
        expectedStats.add(new ShareStat("Windows 8.1", 2L));
        expectedStats.add(new ShareStat("Windows XP", 2L));
        expectedStats.add(new ShareStat("Windows 8", 1L));

        List<ShareStat> resultStats = repository.getUniqueVisitorGroupByOs(25L);
        assertThat(resultStats).hasAtLeastOneElementOfType(ShareStat.class);
        assertThat(resultStats).hasSize(10);
        for (int i = 0; i < 10; i++) {
            ShareStat expected = expectedStats.get(i);
            ShareStat result = resultStats.get(i);
            assertThat(expected.getStatName()).isEqualTo(result.getStatName());
            assertThat(expected.getStatValue()).isEqualTo(result.getStatValue());
        }
    }

    @Test
    public void findBannerOnCampaignPeriodByPriority_shouldWorkProperly() {
        List<Banner> bannerList = repository.findBannerOnCampaignPeriodByPriority();
        assertThat(bannerList.size()).isGreaterThan(3);
    }

}
