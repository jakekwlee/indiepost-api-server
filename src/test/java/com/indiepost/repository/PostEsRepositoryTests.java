package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.model.Post;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.toPostEs;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
public class PostEsRepositoryTests {
    @Inject
    private PostEsRepository postEsRepository;

    @Inject
    private AdminPostRepository adminPostRepository;

    @Test
    public void connectionShouldEstablishProperly() {
        boolean isConnected = postEsRepository.testConnection();
        assertThat(isConnected).isTrue();
    }

    @Test
    public void rebuildIndexShouldWorksProperly() {
        List<Post> posts = adminPostRepository.findScheduledToBeIndexed(LocalDateTime.MIN);
        List<PostEs> postEsList = posts.stream()
                .map(post -> toPostEs(post))
                .collect(Collectors.toList());
        postEsRepository.rebuildIndices(postEsList);
    }
}
