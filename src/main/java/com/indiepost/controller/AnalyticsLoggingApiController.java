package com.indiepost.controller;

import com.indiepost.dto.stat.ActionDto;
import com.indiepost.dto.stat.PageviewDto;
import com.indiepost.service.AnalyticsLoggerService;
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
@RequestMapping("/stat")
public class AnalyticsLoggingApiController {
    private final AnalyticsLoggerService analyticsLoggerService;

    @Autowired
    public AnalyticsLoggingApiController(AnalyticsLoggerService analyticsLoggerService) {
        this.analyticsLoggerService = analyticsLoggerService;
    }

    @PostMapping("/pageview")
    public void logPageview(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody PageviewDto pageviewDto) throws IOException {
        analyticsLoggerService.logPageview(request, response, pageviewDto);
    }

    @PostMapping("/action")
    public void logAction(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestBody ActionDto actionDto) throws IOException {
        analyticsLoggerService.logAction(request, response, actionDto);
    }
}