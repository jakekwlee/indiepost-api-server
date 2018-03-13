package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class ContributorRepositoryTests {

    @Autowired
    private ContributorRepository contributorRepository;

    @Before
    public void beforeTests() {
        Contributor contributor = new Contributor();
        contributor.setName(RandomStringUtils.randomAlphanumeric(10));
        contributor.setEmail("before@example.com");
        contributor.setSubEmail("test_before_sub@example.com");
        contributor.setDescription("Hello World!");
        contributor.setDisplayType(Types.ContributorDisplayType.PLAIN);
        contributor.setTitle("Test Before title");
        contributor.setContributorType(Types.ContributorType.FreelanceEditor);

        LocalDateTime now = LocalDateTime.now();
        contributor.setLastUpdated(now);
        contributor.setCreated(now);

        contributorRepository.save(contributor);
    }

    @Test
    public void save_shouldSaveEntityAndReturnIdProperly() {
        Contributor contributor = new Contributor();
        contributor.setName(RandomStringUtils.randomAlphanumeric(10));
        contributor.setEmail("test@example.com");
        contributor.setSubEmail("test_sub@example.com");
        contributor.setDescription("Hello World");
        contributor.setDisplayType(Types.ContributorDisplayType.PLAIN);
        contributor.setTitle("Test Title");
        contributor.setContributorType(Types.ContributorType.FeatureEditor);

        LocalDateTime now = LocalDateTime.now();
        contributor.setLastUpdated(now);
        contributor.setCreated(now);

        contributorRepository.save(contributor);
        assertThat(contributor.getId()).isNotNull();
    }

    @Test
    public void delete_shouldDeleteContributorProperly() {
        List<Contributor> contributors = contributorRepository.findAll(
                new PageRequest(0, 10, Sort.Direction.DESC, "id")
        ).getContent();
        if (contributors != null && contributors.size() > 0) {
            for (Contributor contributor : contributors) {
                contributorRepository.delete(contributor);
            }
        }
        contributors = contributorRepository.findAll(
                new PageRequest(0, 10, Sort.Direction.DESC, "id")).getContent();
        assertThat(contributors.size()).isEqualTo(0);
    }
}
