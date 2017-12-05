package com.indiepost.controller.api;

import com.indiepost.dto.analytics.ActionDto;
import com.indiepost.dto.analytics.PageviewDto;
import com.indiepost.service.AnalyticsLoggerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/api/stat")
public class AnalyticsLoggingApiController {
    private final AnalyticsLoggerService analyticsLoggerService;

    @Inject
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
