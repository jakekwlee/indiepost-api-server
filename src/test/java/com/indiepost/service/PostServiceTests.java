package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void testFindPostIdByLegacyId() {
        Long legacyId = 10171L;
        Long id = postService.findIdByLegacyId(legacyId);
        PostDto postDto = postService.findOne(id);
        Assert.assertEquals(legacyId, postDto.getLegacyPostId());
    }

    @Test
    public void postsShouldHaveUniqueId() {
        List<PostSummaryDto> posts = postService.find(0, 50, true);
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
        List<PostSummaryDto> posts = postService.find(0, expectedResultLength, true);

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
        List<PostSummaryDto> posts = postService.fullTextSearch(text, 0, 5);
        assertThat(posts).isNotNull().hasSize(5);
        for (PostSummaryDto dto : posts) {
            String titleAndExcerpt = dto.getTitle() + dto.getExcerpt();
            assertThat(titleAndExcerpt).contains(Arrays.asList("em", "단편", "영화"));
        }
        printToJson(posts);

    }

    @Test
    public void testFindByTagName() {
        List<PostSummaryDto> dtoList = postService.findByTagName("일러스트", 0, 50, true);
        assertThat(dtoList.size()).isEqualTo(4);
        printToJson(dtoList);
    }
}
