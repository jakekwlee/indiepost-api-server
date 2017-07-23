package com.indiepost.task;

import com.indiepost.service.AdminPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by jake on 11/22/16.
 */
@Component
public class ScheduledTasksRunner {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasksRunner.class);

    private final AdminPostService adminPostService;

    @Autowired
    public ScheduledTasksRunner(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @Scheduled(fixedRate = 600000)
    public void publishScheduledPosts() {
        adminPostService.publishScheduledPosts();
    }

}