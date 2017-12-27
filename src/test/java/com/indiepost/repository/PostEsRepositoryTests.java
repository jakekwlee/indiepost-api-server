package com.indiepost.repository;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.enums.Types;
import com.indiepost.model.Post;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.toPostEs;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static testHelper.JsonSerializer.printToJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
@Transactional
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PostEsRepositoryTests {

    @Inject
    private PostEsRepository postEsRepository;

    @Inject
    private AdminPostRepository adminPostRepository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void connectionShouldEstablishProperly() {
        boolean isConnected = postEsRepository.testConnection();
        assertThat(isConnected).isTrue();
    }

    @Test
    public void a_rebuildIndexShouldWorksProperly() {
        List<Post> posts = adminPostRepository.findScheduledToBeIndexed(LocalDateTime.MIN);
        List<PostEs> postEsList = posts.stream()
                .map(post -> toPostEs(post))
                .collect(Collectors.toList());
        postEsRepository.rebuildIndices(postEsList);
    }

    @Test
    public void findByIdShouldReturnPostEsCorrectly() {
        Long postId = 5495L;
        PostEs post = postEsRepository.findById(postId);
        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(postId);
        assertThat(post.getTitle()).isNotEmpty();
        assertThat(post.getExcerpt()).isNotEmpty();
        assertThat(post.getBylineName()).isNotEmpty();
        assertThat(post.getContent()).isNotEmpty();
        assertThat(post.getTags().size()).isGreaterThan(0);
        assertThat(post.getContributors().size()).isGreaterThan(0);
        printToJson(post);
    }

    @Test
    public void indexPostEsShouldWorksProperly() {
        Post post = adminPostRepository.findOne(5495L);
        Long indexId = 6000L;
        PostEs postEs = toPostEs(post);
        postEs.setId(indexId);
        postEsRepository.index(postEs);
        PostEs newPost = postEsRepository.findById(indexId);
        assertThat(newPost).isNotNull();
        assertThat(newPost.getId()).isEqualTo(indexId);
        assertThat(newPost.getTitle()).isEqualTo(postEs.getTitle());
        assertThat(newPost.getExcerpt()).isEqualTo(postEs.getExcerpt());
        assertThat(newPost.getBylineName()).isEqualTo(postEs.getBylineName());
        assertThat(newPost.getContent()).isEqualTo(postEs.getContent());
        assertThat(newPost.getTags().size()).isEqualTo(postEs.getTags().size());
        assertThat(newPost.getContributors().size()).isEqualTo(postEs.getContributors().size());
        printToJson(newPost);
    }

    @Test
    public void deleteIndexItemShouldWorkProperly() {
        Long postId = 41L;
        postEsRepository.deleteById(41L);
        PostEs post = postEsRepository.findById(postId);
        assertThat(post).isNull();
    }

    @Test
    public void bulkDeleteWorksProperly() {
        List<Long> ids = Arrays.asList(61L, 71L, 77L, 49L, 43L);
        postEsRepository.bulkDelete(ids);
        for (Long id : ids) {
            PostEs post = postEsRepository.findById(id);
            assertThat(post).isNull();
        }
    }

    @Test
    public void searchTestWorksCorrectly() {
        String text = "무지갯빛 영화";
        List<PostEs> postEsList = postEsRepository.search(
                text,
                Types.PostStatus.PUBLISH,
                new PageRequest(0, 24)
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
        String text = "반 고흐";
        Long userId = 12L;
        User currentUser = userRepository.findById(userId);
        List<PostEs> postEsList = postEsRepository.search(
                text,
                Types.PostStatus.AUTOSAVE,
                currentUser,
                new PageRequest(0, 20)
        );
        printToJson(postEsList);
        assertThat(postEsList).hasSize(2);
        for (PostEs post : postEsList) {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getContent()).isNull();
            assertThat(post.getStatus()).isNull();
            assertThat(post.getTags()).hasSize(0);
            assertThat(post.getContributors()).hasSize(0);
            assertThat(post.getExcerpt()).isNull();
            assertThat(post.getTitle())
                    .isNotEmpty()
                    .contains("em", "반", "고흐");
        }
    }

    @Test
    public void countShouldReturnValueExactly() {
        User currentUser = userRepository.findById(7L);
        Types.PostStatus status = Types.PostStatus.PUBLISH;
        String text = "인디 음악";
        Integer count = postEsRepository.count(text, status, currentUser);
        List<PostEs> posts = postEsRepository.search(text, status, currentUser, new PageRequest(0, 10000));
        assertThat(count).isNotZero().isEqualTo(posts.size());
        printToJson(count);
    }
}
