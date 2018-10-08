package com.indiepost.repository;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
@Transactional
public class PostRepositoryTests {

    @Inject
    private PostRepository postRepository;

    @Test
    public void findByTagName_shouldReturnPostSummaryDtoListProperly() {
        String tagName = "디즈니";
        int pageSize = 5;
        Page<PostSummaryDto> page = postRepository.findByTagName(tagName, PageRequest.of(0, pageSize));
        List<PostSummaryDto> postList = page.getContent();
        assertThat(postList).isNotNull();
        assertThat(postList).hasAtLeastOneElementOfType(PostSummaryDto.class);
        assertThat(postList.size()).isGreaterThanOrEqualTo(pageSize);
        assertThat(page.getSize()).isGreaterThanOrEqualTo(pageSize);
    }

    @Test
    public void findByTagName_resultShouldContainAllElementProperly() {
        String tagName = "디즈니";
        Page<PostSummaryDto> page = postRepository.findByTagName(tagName, PageRequest.of(0, 1));
        List<PostSummaryDto> postList = page.getContent();
        assertThat(postList).isNotNull();
        PostSummaryDto post = postList.get(0);
        assertThat(post).isNotNull();
        assertThat(page.getSize()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void fallbackSearch_returnPostListProperly() {
        Page<PostSummaryDto> page = postRepository.fallbackSearch("황샤오량", PageRequest.of(0, 30));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
    }
}
