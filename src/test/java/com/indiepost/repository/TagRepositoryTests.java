package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class TagRepositoryTests {
    @Inject
    private TagRepository tagRepository;

    @Test
    public void findByNameIn_shouldReturnTagListProperly() {
        List<String> tagNames = Arrays.asList("TV드라마", "2012clothing", "24City");
        List<Tag> tags = tagRepository.findByNameIn(tagNames);
        String[] expected = new String[3];
        String[] result = new String[3];
        tagNames.stream().map(t -> t.toLowerCase()).collect(Collectors.toList()).toArray(expected);
        tags.stream().map(t -> t.getName().toLowerCase()).collect(Collectors.toList()).toArray(result);
        assertThat(result).isEqualTo(expected);
    }
}
