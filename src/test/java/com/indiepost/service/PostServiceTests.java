package com.indiepost.service;

import com.indiepost.NewIndiepostApplicationKt;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static com.indiepost.testHelper.JsonSerializer.printToJson;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by jake on 17. 4. 23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplicationKt.class)
@WebAppConfiguration
public class PostServiceTests {

    @Inject
    private PostService postService;

    @Test
    public void postsShouldHaveUniqueId() {
        List<PostSummaryDto> posts = postService.find(PageRequest.of(0, 10)).getContent();
        Long prevId = -1L;
        for (PostSummaryDto post : posts) {
            assertThat(post.getId()).isNotNull();
            assertThat(post.getId()).isNotEqualTo(prevId);
            prevId = post.getId();
        }
        printToJson(posts);
    }

    @Test
    public void findPostsShouldReturnDtoListProperly() {
        int expected = 50;
        Page<PostSummaryDto> page = postService.find(PageRequest.of(0, expected));
        List<PostSummaryDto> posts = page.getContent();
        assertThat(posts.size()).isEqualTo(expected);
        assertThat(page.getSize()).isEqualTo(expected);
        printToJson(posts);
    }

    @Test
    public void fullTextSearchWorksAsExpected() {
        String text = "단편 영화";
        Page<PostSummaryDto> page = postService.fullTextSearch(text, PageRequest.of(0, 5));
        List<PostSummaryDto> posts = page.getContent();
        assertThat(posts).isNotNull().hasSize(5);
        for (PostSummaryDto dto : posts) {
            Highlight highlight = dto.getHighlight();
            String titleAndExcerpt = highlight.getTitle() + highlight.getExcerpt();
            assertThat(titleAndExcerpt).contains(Arrays.asList("em", "단편", "영화"));
        }
        printToJson(posts);

    }

    @Test
    public void testFindByTagName() {
        Page<PostSummaryDto> page = postService.findByTagName("일러스트", PageRequest.of(0, 100));
        List<PostSummaryDto> dtoList = page.getContent();
        assertThat(dtoList.size()).isGreaterThanOrEqualTo(9);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(9);
        printToJson(dtoList);
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    public void findReadingHistory_shouldReturnResultProperly() {
        TimelineRequest request = new TimelineRequest();
        request.setSize(100);
        request.setTimepoint(Instant.now().toEpochMilli() / 1000);
        long startTime = System.nanoTime();
        Timeline<PostSummaryDto> result = postService.findReadingHistory(request);
        long endTime = System.nanoTime();
        assertThat(result.getContent().size()).isEqualTo(result.getNumberOfElements());
        assertThat(result.getContent().size()).isEqualTo(35);
        System.out.println("Running Time: " + ((endTime - startTime) / 1000000) + "milliseconds");
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    public void moreLikeThis_shouldReturnRelatedPostsProperly() {
        Long id = 7983L;
        int size = 4;
        Page<PostSummaryDto> result = postService.moreLikeThis(id, PageRequest.of(0, 4));
        assertThat(result.getContent().size()).isEqualTo(size);
        printToJson(result);
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    public void findRelatedPostsById_shouldReturnRelatedPostsProperly() {
        Long id = 7983L;
        int size = 4;
        Page<PostSummaryDto> result = postService.findRelatedPostsById(id, PageRequest.of(0, 4));
        assertThat(result.getContent().size()).isEqualTo(size);
        printToJson(result);
    }

    @Test
    @WithMockUser("auth0|5b213cd8064de34cde981b47")
    public void recommendations_shouldReturnRelatedPostsProperly() {
        int size = 16;
        Page<PostSummaryDto> result = postService.recommendations(PageRequest.of(0, size));
        assertThat(result.getContent().size()).isEqualTo(size);
        printToJson(result);
    }
}
