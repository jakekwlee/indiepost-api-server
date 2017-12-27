package com.indiepost.controller.api.admin;

import com.indiepost.service.PostScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/admin/task")
public class ScheduledTaskController {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskController.class);

    @Inject
    private PostScheduledTaskService postScheduledTaskService;


    // once per 10 min
    @PutMapping("/_publish_posts")
    public void publishScheduledPosts() {
        postScheduledTaskService.publishScheduledPosts();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Publish Scheduled Posts:");
    }

    // once per a day
    @PutMapping("/_rebuild_indices")
    public void rebuildElasticsearchIndices() {
        postScheduledTaskService.rebuildElasticsearchIndices();
    }

    @PutMapping("/_cache_stat")
    public void updateCachedPostStats() {
        postScheduledTaskService.updateCachedPostStats();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Update cached post stats.");
    }
}
