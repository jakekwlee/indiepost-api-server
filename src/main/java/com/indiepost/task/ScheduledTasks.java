package com.indiepost.task;

import com.indiepost.service.AdminPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Created by jake on 11/22/16.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AdminPostService adminPostService;


    //    @Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 0/30 * * * ?")
    public void publishPosts() {
        adminPostService.publishPosts();
    }
}