package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.LinkDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class CampaignRepositoryTests {
    @Autowired
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

}
