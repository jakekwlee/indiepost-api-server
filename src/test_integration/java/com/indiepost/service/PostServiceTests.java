package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostSummary;
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
        PostDto postDto = postService.findById(id);
        Assert.assertEquals(legacyId, postDto.getLegacyPostId());
    }

    @Test
    public void postsShouldHaveUniqueId() {
        List<PostSummary> posts =  postService.findAll(0, 50, true);
        Long id = -1L;
        for (PostSummary post : posts) {
            Assert.assertNotEquals(id, post.getId());
            id = post.getId();
        }
    }
}
