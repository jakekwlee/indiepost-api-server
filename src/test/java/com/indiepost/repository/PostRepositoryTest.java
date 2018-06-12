package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class PostRepositoryTest {

    @Inject
    private PostRepository repository;

    @Test
    public void findByIdsShouldReturnPostsInRequestedOrder() {
        List<Long> postIds = Arrays.asList(5891L, 1679L, 5897L, 5929L);
        List<PostSummaryDto> posts = repository.findByIds(postIds);
        List<Long> resultIds = posts.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
        assertThat(postIds).isEqualTo(resultIds);
    }

    @Test
    public void getStatusByIdShouldReturnPostStatusCorrectly() {
        Long postId = 5929L;
        Types.PostStatus status = repository.getStatusById(postId);
        assertThat(status).isEqualTo(Types.PostStatus.PUBLISH);
    }
}
