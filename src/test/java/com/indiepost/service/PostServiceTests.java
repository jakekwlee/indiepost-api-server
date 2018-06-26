package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.utils.DomUtil;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static testHelper.JsonSerializer.printToJson;

/**
 * Created by jake on 17. 4. 23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
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
        assertThat(page.getTotalElements()).isEqualTo(5);
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
        assertThat(dtoList.size()).isEqualTo(9);
        assertThat(page.getTotalElements()).isEqualTo(9);
        printToJson(dtoList);
    }

    @Test
    public void findById_shouldReturnPostDtoWithRelatedPostsProperly() {
        PostDto post = postService.findOne(908L);
        assertThat(post).isNotNull();
        assertThat(post.getRelatedPostIds()).isNotNull();
        assertThat(post.getRelatedPostIds().size()).isGreaterThan(1);
        assertThat(DomUtil.relatedPostsPattern.matcher(post.getContent()).find()).isFalse();
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
}
