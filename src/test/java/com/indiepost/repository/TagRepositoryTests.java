package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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

    @Test
    public void saveWorksProperly() {
        String tagName = RandomStringUtils.randomAlphabetic(10);
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);

        assertThat(tag.getId()).isNotNull();
        assertThat(tag.getName()).isEqualTo(tagName);
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

    @Test
    public void searchTagsWorksProperly() {
        int MAX_RESULTS = 24;
        String text1 = "이민휘";
        String text2 = "영";
        List<TagDto> tags1 = tagRepository.search(text1, new PageRequest(0, MAX_RESULTS));
        printToJson(tags1);
        List<TagDto> tags2 = tagRepository.search(text2, new PageRequest(0, MAX_RESULTS));
        printToJson(tags2);

        assertThat(tags1).isNotNull().hasSize(1);
        assertThat(tags1.get(0).getName()).isEqualTo("이민휘");
        assertThat(tags2).isNotNull().hasSize(MAX_RESULTS);
        assertThat(tags2.get(0).getName()).isEqualTo("영감");
        assertThat(tags2.get(1).getName()).isEqualTo("영국");
    }

}
