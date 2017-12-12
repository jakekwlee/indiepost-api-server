package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.TagDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.util.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class TagServiceTests {

    @Inject
    private TagService tagService;

    private List<Long> insertedIds = new ArrayList<>();

    @Test
    public void findByIdsShouldReturnTagsWithRequestedOrder() {
        List<Long> tagIds = Arrays.asList(1198L, 1206L, 1544L, 2062L, 4032L);
        List<TagDto> tags = tagService.findByIds(tagIds);
        List<Long> actualIds = tags.stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toList());

        assertThat(tags).isNotNull();
        assertThat(actualIds).isEqualTo(tagIds);
        tags.forEach(tagDto ->
                assertThat(tagDto.getName()).isNotNull()
        );
    }

    @Test
    public void saveTagWorksCorrectly() {
        String tagName = StringUtils.randomAlphanumeric(10);
        TagDto tagIn = new TagDto(tagName);

        TagDto tagOut = tagService.save(tagIn);
        printToJson(tagOut);
        insertedIds.add(tagOut.getId());

        assertThat(tagOut).isNotNull();
        assertThat(tagOut.getId()).isNotNull();
        assertThat(tagOut.getName()).isEqualTo(tagName);
    }

    @After
    public void clearTestData() {
        if (insertedIds.size() > 0) {
            for (Long id : insertedIds) {
                tagService.deleteById(id);
            }
        }
    }
}
