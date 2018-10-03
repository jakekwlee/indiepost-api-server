package com.indiepost.repository;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
@Transactional
public class ContributorRepositoryTests {

    @Inject
    private ContributorRepository contributorRepository;

    private List<Long> insertedId = new ArrayList<>();


    @Before
    public void beforeTests() {
        Contributor contributor = new Contributor();
        contributor.setFullName(RandomStringUtils.randomAlphanumeric(10));
        contributor.setEmail("before@example.com");
        contributor.setSubEmail("test_before_sub@example.com");
        contributor.setDescription("Hello World!");
        contributor.setDisplayType(Types.ContributorDisplayType.TEXT);
        contributor.setTitle("Test Before title");
        contributor.setContributorType(Types.ContributorType.FreelanceEditor);

        LocalDateTime now = LocalDateTime.now();
        contributor.setLastUpdated(now);
        contributor.setCreated(now);

        Long id = contributorRepository.save(contributor);
        insertedId.add(id);
    }

    @Test
    public void save_shouldSaveEntityAndReturnIdProperly() {
        Contributor contributor = new Contributor();
        contributor.setFullName(RandomStringUtils.randomAlphanumeric(10));
        contributor.setEmail("test@example.com");
        contributor.setSubEmail("test_sub@example.com");
        contributor.setDescription("Hello World");
        contributor.setDisplayType(Types.ContributorDisplayType.TEXT);
        contributor.setTitle("Test Title");
        contributor.setContributorType(Types.ContributorType.FeatureEditor);

        LocalDateTime now = LocalDateTime.now();
        contributor.setLastUpdated(now);
        contributor.setCreated(now);

        Long id = contributorRepository.save(contributor);
        if (id != null) {
            insertedId.add(id);
        }
        assertThat(id).isNotNull();
    }

    @Test
    public void delete_shouldDeleteContributorProperly() {
        List<Contributor> contributors = contributorRepository.findAll(
                PageRequest.of(0, 10, Sort.Direction.DESC, "id")
        ).getContent();
        if (contributors.size() > 0) {
            for (Contributor contributor : contributors) {
                contributorRepository.delete(contributor);
            }
        }
        contributors = contributorRepository.findAll(
                PageRequest.of(0, 10, Sort.Direction.DESC, "id")).getContent();
        assertThat(contributors.size()).isEqualTo(0);
    }

    @After
    public void afterTests() {
        for (Long id : insertedId) {
            contributorRepository.deleteById(id);
        }
    }
}
