package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSummaryDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by jake on 17. 4. 23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostServiceTests {

    @Autowired
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

    }
}
