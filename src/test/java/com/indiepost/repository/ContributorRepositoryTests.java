package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class ContributorRepositoryTests {
    @Inject
    private ContributorRepository contributorRepository;

    private List<Long> insertedIds = new ArrayList<>();

    @Test
    public void saveWorksProperly() {
        Contributor contributor = new Contributor();
        String name = "test name";
        String email = "test@test.com";
        String tel = "01093331010";
        String about = StringUtils.randomAlphanumeric(200);
        String description = StringUtils.randomAlphanumeric(100);
        LocalDateTime now = LocalDateTime.now();
        Types.ContributorRole role = Types.ContributorRole.FeatureEditor;

        contributor.setName(name);
        contributor.setEmail(email);
        contributor.setPhone(tel);
        contributor.setAbout(about);
        contributor.setCreatedAt(now);
        contributor.setModifiedAt(now);
        contributor.setDescription(description);
        contributor.setRole(role);

        contributorRepository.save(contributor);
        insertedIds.add(contributor.getId());
        printToJson(contributor);

        assertThat(contributor).isNotNull();
        assertThat(contributor.getId()).isNotNull();
        assertThat(contributor.getAbout()).isEqualTo(about);
        assertThat(contributor.getDescription()).isEqualTo(description);
        assertThat(contributor.getName()).isEqualTo(name);
        assertThat(contributor.getEmail()).isEqualTo(email);
        assertThat(contributor.getRole()).isEqualTo(role);
        assertThat(contributor.getCreatedAt()).isEqualTo(now);
        assertThat(contributor.getModifiedAt()).isEqualTo(now);
    }

    @After
    public void deleteAllTestDataAfterTestDone() {
        if (insertedIds.size() > 0) {
            for (Long id : insertedIds) {
                contributorRepository.deleteById(id);
            }
        }
    }
}
