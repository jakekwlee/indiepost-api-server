package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostSummaryDto;
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
        List<Long> postIds = Arrays.asList(220L, 3424L, 864L, 86L, 53L);
        List<PostSummaryDto> posts = repository.findByIds(postIds);
        List<Long> resultIds = posts.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
        assertThat(postIds).isEqualTo(resultIds);
    }
}
