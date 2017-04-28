package com.indiepost.controller.api;

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/api/stat")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @PostMapping("/pageview")
    public void logPageview(HttpServletRequest request, @RequestBody Pageview pageview) throws IOException {
        analyticsService.logPageview(request, pageview);
    }

    @PostMapping("/action")
    public void logAction(HttpServletRequest request, @RequestBody Action action) throws IOException {
        analyticsService.logAction(request, action);
    }
}
