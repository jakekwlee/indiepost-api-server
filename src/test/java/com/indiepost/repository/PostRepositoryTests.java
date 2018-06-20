package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
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
}
