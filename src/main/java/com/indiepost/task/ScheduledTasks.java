package com.indiepost.task;

import com.indiepost.service.AdminPostService;
import com.indiepost.service.SitemapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jake on 11/22/16.
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
        log.info(dateFormat.format(new Date()) + ": Create Sitemap: /sitemap.xml");
        sitemapService.createSitemap();
    }
}