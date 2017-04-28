package com.indiepost.service;

import com.indiepost.NewIndiepostApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
}
