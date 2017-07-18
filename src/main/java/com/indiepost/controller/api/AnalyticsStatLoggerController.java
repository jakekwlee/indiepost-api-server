package com.indiepost.controller.api;

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.service.AnalyticsStatLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/api/stat")
public class AnalyticsStatLoggerController {
    private final AnalyticsStatLoggerService analyticsStatLoggerService;

    @Autowired
    public AnalyticsStatLoggerController(AnalyticsStatLoggerService analyticsStatLoggerService) {
        this.analyticsStatLoggerService = analyticsStatLoggerService;
    }

    @PostMapping("/pageview")
    public void logPageview(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody Pageview pageview) throws IOException {
        analyticsStatLoggerService.logPageview(request, response, pageview);
    }

    @PostMapping("/action")
    public void logAction(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestBody Action action) throws IOException {
        analyticsStatLoggerService.logAction(request, response, action);
    }
}
