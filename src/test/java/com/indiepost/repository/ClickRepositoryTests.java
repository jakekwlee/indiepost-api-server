package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jake on 8/10/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class ClickRepositoryTests {

    @Inject
    private ClickRepository repository;

    @Test
    public void countAllClicksByCampaignId_shouldReturnExpectedValue() {
        Long clicks = repository.countAllClicksByCampaignId(28L);
        assertThat(clicks).isGreaterThanOrEqualTo(218);
    }

    @Test
    public void countValidClicksByCampaignId_shouldReturnExpectedValue() {
        Long clicks = repository.countValidClicksByCampaignId(28L);
        assertThat(clicks).isGreaterThanOrEqualTo(183);
    }
}
