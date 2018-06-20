package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
import com.indiepost.model.User;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static com.indiepost.testHelper.JsonSerializer.printToJson;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostEsRepositoryTests {

    @Inject
    private PostEsRepository postEsRepository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void connectionShouldEstablishProperly() {
        boolean isConnected = postEsRepository.testConnection();
        assertThat(isConnected).isTrue();
    }

    @Test
    public void findByIdShouldReturnPostEsCorrectly() {
        Long postId = 6740L;
        PostEs post = postEsRepository.findById(postId);
        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(postId);
        assertThat(post.getTitle()).isNotEmpty();
        assertThat(post.getExcerpt()).isNotEmpty();
        assertThat(post.getBylineName()).isNotEmpty();
        assertThat(post.getContent()).isNotEmpty();
        assertThat(post.getTags().size()).isGreaterThan(0);
        printToJson(post);
    }


    @Test
    public void searchTestWorksCorrectly() {
        String text = "무지갯빛 영화";
        List<PostEs> postEsList = postEsRepository.search(
                text,
                Types.PostStatus.PUBLISH,
                PageRequest.of(0, 24)
        );
        assertThat(postEsList).isNotNull().hasSize(1);
        PostEs post = postEsList.get(0);
        assertThat(post.getId())
                .isEqualTo(287L);
        assertThat(post.getContent()).isNull();
        assertThat(post.getStatus()).isNull();
        assertThat(post.getTags()).hasSize(0);
        assertThat(post.getContributors()).hasSize(0);
        assertThat(post.getTitle())
                .isNotEmpty()
                .contains("em", "무지갯", "영화");
        assertThat(post.getExcerpt())
                .isNotEmpty()
                .contains("em", "영화");
        printToJson(postEsList);
    }

    @Test
    public void searchForAdminUserWorksCorrectly() {
        String text = "너의 췌장을";
        Long userId = 12L;
        User currentUser = userRepository.findById(userId);
        List<PostEs> postEsList = postEsRepository.search(
                text,
                Types.PostStatus.PUBLISH,
                currentUser,
                PageRequest.of(0, 20)
        );
        printToJson(postEsList);
        assertThat(postEsList).hasSize(2);
        for (PostEs post : postEsList) {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getContent()).isNull();
            assertThat(post.getStatus()).isNull();
            assertThat(post.getTags()).hasSize(0);
            assertThat(post.getExcerpt()).isNull();
        }
    }

    @Test
    public void countShouldReturnValueExactly() {
        User currentUser = userRepository.findById(7L);
        Types.PostStatus status = Types.PostStatus.PUBLISH;
        String text = "인디 음악";
        Integer count = postEsRepository.count(text, status, currentUser);
        List<PostEs> posts = postEsRepository.search(text, status, currentUser, PageRequest.of(0, 10000));
        assertThat(count).isNotZero().isEqualTo(posts.size());
        printToJson(count);
    }
}
