package com.indiepost.task;

import com.indiepost.service.AdminPostService;
import com.indiepost.service.SitemapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by jake on 11/22/16.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private final AdminPostService adminPostService;

    private final SitemapService sitemapService;

    @Autowired
    public ScheduledTasks(AdminPostService adminPostService, SitemapService sitemapService) {
        this.adminPostService = adminPostService;
        this.sitemapService = sitemapService;
    }

    @Scheduled(fixedRate = 60000)
    public void publishScheduledPosts() {
        adminPostService.publishScheduledPosts();
    }

    @Scheduled(fixedRate = 3600000)
    public void createSitemap() throws MalformedURLException {
        log.info(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ": Create Sitemap: /sitemap.xml");
        sitemapService.createSitemap();
    }
}