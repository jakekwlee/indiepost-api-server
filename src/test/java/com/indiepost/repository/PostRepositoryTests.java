package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
        List<PostSummaryDto> postList = postRepository.findByTagName(tagName, new PageRequest(0, pageSize));
        assertThat(postList).isNotNull();
        assertThat(postList).hasAtLeastOneElementOfType(PostSummaryDto.class);
        assertThat(postList.size()).isGreaterThanOrEqualTo(pageSize);
    }

    @Test
    public void findByTagName_resultShouldContainAllElementProperly() {
        String tagName = "디즈니";
        List<PostSummaryDto> postList = postRepository.findByTagName(tagName, new PageRequest(0, 1));
        assertThat(postList).isNotNull();
        PostSummaryDto post = postList.get(0);
        assertThat(post).isNotNull();
    }
}
