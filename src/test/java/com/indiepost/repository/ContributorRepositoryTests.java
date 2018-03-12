package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ContributorRepositoryTests {

    @Autowired
    private ContributorRepository contributorRepository;

    @Before
    public void beforeTests() {
        Contributor contributor = new Contributor();
        contributor.setName("Test Before");
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
        contributor.setName("Test Name");
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
        // TODO
    }
}
