package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;
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
        List<PostSummaryDto> posts = postService.find(PageRequest.of(0, 10));
        Long id = -1L;
        for (PostSummaryDto post : posts) {
            Assert.assertNotEquals(id, post.getId());
            id = post.getId();
        }
        printToJson(posts);
    }

    @Test
    public void findPostsShouldReturnDtoListProperly() {
        int expectedResultLength = 50;
        List<PostSummaryDto> posts = postService.find(PageRequest.of(0, expectedResultLength));

        Assert.assertEquals(
                "Size of List<PostSummaryDto> should same as expected",
                expectedResultLength,
                posts.size()
        );
        printToJson(posts);
    }

    @Test
    public void fullTextSearchWorksAsExpected() {
        String text = "단편 영화";
        FullTextSearchQuery query = new FullTextSearchQuery(text, Types.PostStatus.PUBLISH.toString(), 0, 5);
        List<PostSummaryDto> posts = postService.fullTextSearch(query);
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
        List<PostSummaryDto> dtoList = postService.findByTagName("일러스트", PageRequest.of(0, 100));
        assertThat(dtoList.size()).isEqualTo(9);
        printToJson(dtoList);
    }
}
