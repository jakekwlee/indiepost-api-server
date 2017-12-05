package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class TagRepositoryTests {
    @Inject
    private TagRepository tagRepository;

    private List<Long> insertedIds = new ArrayList<>();

    @Test
    public void saveWorksProperly() {
        String tagName = RandomStringUtils.randomAlphabetic(10);
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);

        assertThat(tag.getId()).isNotNull();
        assertThat(tag.getName()).isEqualTo(tagName);
        insertedIds.add(tag.getId());
        printToJson(tag);
    }

    @Test
    public void findByIdInShouldReturnTagsInRequestedOrder() {
        List<Long> ids = Arrays.asList(3173L, 3163L, 4178L, 2287L, 2115L, 28L, 604L);
        List<Tag> tags = tagRepository.findByIdIn(ids);
        List<Long> resultIds = tags.stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());
        assertThat(resultIds).isEqualTo(ids);
    }

    @After
    public void deleteTestTags() {
        if (insertedIds.size() > 0) {
            for (Long id : insertedIds) {
                tagRepository.deleteById(id);
            }
        }
    }
}
