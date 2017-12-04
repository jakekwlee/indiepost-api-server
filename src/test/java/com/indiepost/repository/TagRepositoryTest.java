package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Tag;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class TagRepositoryTest {
    @Inject
    private TagRepository tagRepository;

    @Test
    public void findByIdInShouldReturnTagsInRequestedOrder() {
        List<Long> ids = Arrays.asList(3173L, 3163L, 4178L, 2287L, 2115L, 28L, 604L);
        List<Tag> tags = tagRepository.findByIdIn(ids);
        List<Long> resultIds = tags.stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());
        Assertions.assertThat(resultIds).isEqualTo(ids);
    }
}
