package com.indiepost.task;

import com.indiepost.service.AnalyticsService;
import com.indiepost.service.PostScheduledTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by jake on 11/22/16.
 */
@Component
public class ScheduledTasksRunner {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasksRunner.class);

    private final PostScheduledTaskService postScheduledTaskService;

    private final AnalyticsService analyticsService;

    @Autowired
    public ScheduledTasksRunner(PostScheduledTaskService postScheduledTaskService, AnalyticsService analyticsService) {
        this.postScheduledTaskService = postScheduledTaskService;
        this.analyticsService = analyticsService;
    }

    @Scheduled(fixedRate = 600000)
    public void publishScheduledPosts() {
        postScheduledTaskService.publishScheduledPosts();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Publish Scheduled Posts:");
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateCachedPostStats() {
        analyticsService.updateCachedPostStats();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Update cached post stats.");
    }
}
