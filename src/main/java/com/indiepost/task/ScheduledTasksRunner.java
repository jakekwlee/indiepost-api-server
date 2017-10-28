package com.indiepost.task;

import com.indiepost.service.AdminPostService;
import com.indiepost.service.AnalyticsService;
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

    private final AdminPostService adminPostService;

    private final AnalyticsService analyticsService;

    @Autowired
    public ScheduledTasksRunner(AdminPostService adminPostService, AnalyticsService analyticsService) {
        this.adminPostService = adminPostService;
        this.analyticsService = analyticsService;
    }

    @Scheduled(fixedRate = 600000)
    public void publishScheduledPosts() {
        adminPostService.publishScheduledPosts();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Publish Scheduled Posts:");
    }

    @Scheduled(cron = "* 0,30 * * * *")
    public void updateCachedPostStats() {
        analyticsService.updateCachedPostStats();
        log.info(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ": Update cached post stats.");
    }
}
