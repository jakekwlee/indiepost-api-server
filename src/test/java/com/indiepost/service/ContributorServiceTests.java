package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.ContributorDto;
import com.indiepost.enums.Types;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.util.StringUtils;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class ContributorServiceTests {

    private static final Logger log = LoggerFactory.getLogger(ContributorServiceTests.class);

    private List<Long> insertedIds = new ArrayList<>();

    @Inject
    private ContributorService contributorService;

    @Test
    public void findByIdsShouldReturnContributorsWithRequestedOrder() {
        List<Long> contributorIds = Arrays.asList(2L, 1L, 6L);
        List<ContributorDto> contributors = contributorService.findByIds(contributorIds);
        List<Long> actualIds = contributors.stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toList());

        assertThat(contributors).isNotNull();
        assertThat(actualIds).isEqualTo(contributorIds);
        contributors.forEach(contributorDto ->
                AssertionsForClassTypes.assertThat(contributorDto.getName()).isNotNull()
        );
    }

    @Test
    public void saveContributorWorksCorrectly() {
        ContributorDto dto = new ContributorDto();
        String name = "test name";
        String email = "test@test.com";
        String tel = "01093331010";
        String about = StringUtils.randomAlphanumeric(200);
        String description = StringUtils.randomAlphanumeric(100);
        LocalDateTime now = LocalDateTime.now();
        Types.ContributorRole role = Types.ContributorRole.FeatureEditor;

        dto.setName(name);
        dto.setEmail(email);
        dto.setPhone(tel);
        dto.setAbout(about);
        dto.setCreatedAt(now);
        dto.setModifiedAt(now);
        dto.setDescription(description);
        dto.setRole(role.toString());


        log.info("ContributorDto from client:");
        printToJson(dto);

        ContributorDto result = contributorService.save(dto);

        log.info("Saved contributorDto:");
        printToJson(result);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getAbout()).isEqualTo(about);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getPhone()).isEqualTo(tel);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getRole()).isEqualTo(role.toString());
        assertThat(result.getModifiedAt()).isEqualTo(now);
        assertThat(result.getCreatedAt()).isEqualTo(now);

        insertedIds.add(result.getId());
    }

    @After
    public void cleatTestData() {
        if (insertedIds.size() > 0) {
            insertedIds.forEach(id -> contributorService.deleteById(id));
        }
    }

}
