package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import com.indiepost.dto.PostDto;
import com.indiepost.utils.DomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by jake on 17. 4. 23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    public void testFindPostIdByLegacyId() {
        Long legacyId = 10171L;
        Long id = postService.findIdByLegacyId(legacyId);
        System.out.println("===================================");
        System.out.println("Input:" + legacyId);
        System.out.println("Output:" + id);
        System.out.println("===================================");
    }

    @Test
    public void findById_shouldReturnPostDtoWithRelatedPostsProperly() {
        PostDto post = postService.findById(908L);
        assertThat(post).isNotNull();
        assertThat(post.getRelatedPostIds()).isNotNull();
        assertThat(post.getRelatedPostIds().size()).isGreaterThan(1);
        assertThat(DomUtil.relatedPostsPattern.matcher(post.getContent()).find()).isFalse();
    }
}