package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.service.AdminPostServiceTests.areListContentsEqual;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class TagServiceTest {

    @Inject
    private TagService tagService;

    @Test
    public void findByIdsShouldReturnTagsProperly() {
        List<Long> tagIds = Arrays.asList(4280L, 4279L);
        List<Tag> tags = tagService.findByIds(tagIds);
        assertThat(tags, notNullValue());
        assertThat(tags.size(), is(tagIds.size()));
        List<Long> actualIds = tags.stream()
                .map(tag -> tag.getId())
                .collect(Collectors.toList());
        assertTrue(areListContentsEqual(tagIds, actualIds));
    }
}
