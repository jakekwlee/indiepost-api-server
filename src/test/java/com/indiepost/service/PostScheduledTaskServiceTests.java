package com.indiepost.service;


import com.indiepost.NewIndiepostApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.inject.Inject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NewIndiepostApplication.class)
@WebAppConfiguration
public class PostScheduledTaskServiceTests {

    private static final Logger log = LoggerFactory.getLogger(PostScheduledTaskService.class);

    @Inject
    private PostScheduledTaskService postScheduledTaskService;

    @Test
    public void updateCachedPostsWorkProperly() {
        long start = System.nanoTime();
        postScheduledTaskService.updateCachedPostStats();
        long end = System.nanoTime();
        log.info("Running time: " + (end - start) * Math.pow(10, 9));
    }

    @Test
    public void updateElasticsearchIndicesUpdatesIndicesProperly() {
        long start = System.nanoTime();
//        postScheduledTaskService.rebuildElasticsearchIndices();
        long end = System.nanoTime();
        log.info("Running time: " + (end - start) * Math.pow(10, 9));

    }

    @Test
    public void searchWorksCorrectly() {
//        List<PostEs> posts = postEsService.search("일상 무지갯빛", Types.PostStatus.PUBLISH, 0, 10);
    }

}
